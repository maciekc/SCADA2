import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";

import { PlantService } from '../../services/plant-service/plant.service';
import { Figure, LineFigure } from '../../class/figure';

declare var Plotly: any;

@Component({
  selector: 'app-plant',
  templateUrl: './plant.component.html',
  styleUrls: ['./plant.component.css']
})
export class PlantComponent implements OnInit, OnDestroy {

  private serviceSubscriptions: Subscription [] = [];
  
  private outputFigure: Figure;
  private outputFigureId: HTMLElement;  
  private stateVariableFigure: Figure;
  private stateVariableFigureId: HTMLElement;  


  constructor(private plantService: PlantService) {}
  
  changeStateVariable(event) {
    let target = event.target;
    let id = target.id;
    document.getElementById("SVDropButton").innerText = target.innerText;
    this.plantService.chanegStateVariablePlotTab(id)
  }

  ngOnInit() {
    console.log("on init")
    this.initFigures()

    this.plantService.startService()
    
    let output = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.plantService.getOutputDates();
      let values = this.plantService.getOutputValues()
      this.outputFigure.updatePlotData(dates, values, 'wyjście');
    })
    this.serviceSubscriptions.push(output);

    let stateVariable = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.plantService.getStateVariableDates();
      let values = this.plantService.getStateVariableValues()
      this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]', 'orange');
    })
    this.serviceSubscriptions.push(stateVariable);


    
  }

  ngOnDestroy() {
    console.log("on destroy")
    this.plantService.stopService();
    this.stopService()
  }

  private stopService() {
    this.serviceSubscriptions.forEach(s => s.unsubscribe());
    this.serviceSubscriptions = [];
  }

  private initFigures() {
    this.outputFigureId = document.getElementById('outputPlot');
    this.outputFigure = new LineFigure(this.outputFigureId, "Czas [s]", "Stężenie [%]");

    this.stateVariableFigureId = document.getElementById('plantPlot');
    this.stateVariableFigure = new LineFigure(this.stateVariableFigureId, "Czas [s]", "Poziom [cm]");
  }
}

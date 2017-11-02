import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";

import { ControllerService } from '../../services/controller-service/controller-service.service';
import { Figure, LineFigure } from '../../class/figure';

declare var Plotly: any;

@Component({
  selector: 'app-controler',
  templateUrl: './controler.component.html',
  styleUrls: ['./controler.component.css']
})
export class ControlerComponent implements OnInit, OnDestroy {

  private serviceSubscriptions: Subscription [] = [];
  
  private controllerFigure: Figure;
  private controllerFigureId: HTMLElement;
  private stateVariableFigure: Figure;
  private stateVariableFigureId: HTMLElement;  


  constructor(private conrtollerService: ControllerService) {}
  
  changeStateVariable(event) {
    let target = event.target;
    let id = target.id;
    document.getElementById("SVDropButton").innerText = target.innerText;
    this.conrtollerService.chanegStateVariablePlotTab(id)
  }

  changeController(event) {
    let target = event.target;
    let id = target.id; 
    document.getElementById("ConDropButton").innerText = target.innerText;
    this.conrtollerService.chanegControllPlotTab(id)
  }

  ngOnInit() {
    console.log("on init")
    this.initFigures()

    this.conrtollerService.startService()
    
    let controller = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.conrtollerService.getControllerDates();
      let values = this.conrtollerService.getControllerValues()
      this.controllerFigure.updatePlotData(dates, values, 'Sterowanie', 'green');
    })
    this.serviceSubscriptions.push(controller);

    let stateVariable = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.conrtollerService.getStateVariableDates();
      let values = this.conrtollerService.getStateVariableValues()
      this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]', 'orange');
    })
    this.serviceSubscriptions.push(stateVariable);
 
  }

  ngOnDestroy() {
    console.log("on destroy")
    this.conrtollerService.stopService();
    this.stopService()
  }

  private stopService() {
    this.serviceSubscriptions.forEach(s => s.unsubscribe());
    this.serviceSubscriptions = [];
  }

  private initFigures() {
    this.controllerFigureId = document.getElementById('controlPlot');
    this.controllerFigure = new LineFigure(this.controllerFigureId, "Czas [s]", "Sterowanie");

    // this.controllerFigureId = document.getElementById('controlPlot');
    // this.controllerFigure = new Figure(this.controllerFigureId, "Czas [s]", "Sterowanie [V]");

    this.stateVariableFigureId = document.getElementById('plantPlot');
    this.stateVariableFigure = new LineFigure(this.stateVariableFigureId, "Czas [s]", "Poziom [cm]");
  }



}

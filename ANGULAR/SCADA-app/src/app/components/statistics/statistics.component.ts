import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";

import { StateVariablesService } from '../../services/stateVariableService/state-variables.service';
import { StatisticService } from '../../services/statistic-service/statistic-service.service';
import { Figure } from '../../class/figure';

declare var Plotly: any;

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit, OnDestroy {

  private serviceSubscriptions: Subscription [] = [];
  private outputFigure: Figure;
  private outputFigureId: HTMLElement;  
  private controllerFigure: Figure;
  private controllerFigureId: HTMLElement;
  private stateVariableFigure: Figure;
  private stateVariableFigureId: HTMLElement;  


  constructor(private statisticService: StatisticService) {}
  
  changeStateVariable(event) {
    let target = event.target;
    let id = target.id;
    document.getElementById("SVDropButton").innerText = target.innerText;
    this.statisticService.chanegStateVariablePlotTab(id)
  }

  changeController(event) {
    let target = event.target;
    let id = target.id; 
    document.getElementById("ConDropButton").innerText = target.innerText;
    this.statisticService.chanegControllPlotTab(id)
  }

  ngOnInit() {
    console.log("on init")
    this.initFigures()

    this.statisticService.startService()
    
    let output = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.statisticService.getOutputDates();
      let values = this.statisticService.getOutputValues()
      this.outputFigure.updatePlotData(dates, values, 'wyjście');
    })

    this.serviceSubscriptions.push(output);

    let controller = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.statisticService.getControllerDates();
      let values = this.statisticService.getControllerValues()
      this.controllerFigure.updatePlotData(dates, values, 'sterowanie', 'orange');
    })
    this.serviceSubscriptions.push(controller);

    let stateVariable = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.statisticService.getStateVariableDates();
      let values = this.statisticService.getStateVariableValues()
      this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]');
    })
    this.serviceSubscriptions.push(stateVariable);


    let data = [{
      line: {color: "blue"},
      mode: "lines",
      name: "wyjście",
      type: "scatter",
      x: [1, 2, 3, 4, 5],
      y: [1, 2, 4, 8, 16],
    }];
    let layout = {
      margin: { t: 0 },
      xaxis : {title : 'Czas [s]'},
      yaxis : {title : 'Napięcie [V]'}
    }

    // let figure3 = document.getElementById('plantPlot');
    // Plotly.newPlot(figure3, data, layout);
    
    let figure4 = document.getElementById('controlerPlot');
    Plotly.newPlot(figure4, data, layout);
  }

  ngOnDestroy() {
    console.log("on destroy")
    this.statisticService.stopService();
    this.stopService()
  }

  private stopService() {
    this.serviceSubscriptions.forEach(s => s.unsubscribe());
    this.serviceSubscriptions = [];
  }

  private initFigures() {
    this.outputFigureId = document.getElementById('outputPlot');
    this.outputFigure = new Figure(this.outputFigureId, "Czas [s]", "Stężenie [%]");

    this.controllerFigureId = document.getElementById('controlPlot');
    this.controllerFigure = new Figure(this.controllerFigureId, "Czas [s]", "Sterowanie [V]");

    this.stateVariableFigureId = document.getElementById('plantPlot');
    this.stateVariableFigure = new Figure(this.stateVariableFigureId, "Czas [s]", "Poziom [cm]");
  }



}

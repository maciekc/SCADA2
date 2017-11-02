import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";

import { StateVariablesService } from '../../services/stateVariableService/state-variables.service';
import { StatisticService } from '../../services/statistic-service/statistic-service.service';
import { LineFigure, AreaFigure, BarFigure, Figure } from '../../class/figure';
import { Label } from '../../class/label';
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

  private stateVariableFigure: Figure;
  private stateVariableFigureId: HTMLElement;

  private productionFigure: Figure;
  private productionFigureId: HTMLElement;

  private concentrationLabelId: HTMLElement;
  private concentrationLabel: Label;
  private currentProductionLabelId: HTMLElement;
  private currentProductionLabel: Label;

  // private currentConcentration: number = 0;
  // private currentLevel: number = 0;

  constructor(private statisticService: StatisticService) {}
  
  changeStateVariable(event) {
    let target = event.target;
    let id = target.id;
    document.getElementById("SVDropButton").innerText = target.innerText;
    this.statisticService.chanegStateVariablePlotTab(id)
  }

  changeProductionFigureMode(event) {
    let target = event.target;
    let id = target.id;
    document.getElementById("SVDropButton").innerText = target.innerText;
    //BAR_HOUR
    //BAR_DAY
    this.statisticService.changeProductionFigureMode(id)
  }


  ngOnInit() {
    console.log("on init")
    this.initFigures()

    this.statisticService.startService()
    
    let output = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.statisticService.getOutputDates();
      let values = this.statisticService.getOutputValues();
      this.concentrationLabel.setLabelValue(this.statisticService.getCurrentConcentration());
      this.outputFigure.updatePlotData(dates, values, 'wyjście');
    })
    this.serviceSubscriptions.push(output);

    let stateVariable = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.statisticService.getStateVariableDates();
      let values = this.statisticService.getStateVariableValues()
      this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]', 'orange');
    })
    this.serviceSubscriptions.push(stateVariable);

    let productionVariable = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.statisticService.getProductionDates();
      let values = this.statisticService.getProductionValues();
      let currentLevel = values[values.length - 1];

      //tozeroy - niebieski
      //tonexty - czerwony
      console.log(currentLevel)
      console.log(values.length)
      this.productionFigure.updatePlotData(dates, values, 'Produkcja', 'tonexty');
      this.currentProductionLabel.setLabelValue(this.statisticService.getCurrentProduction());
    })
    this.serviceSubscriptions.push(productionVariable);
    
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
    this.outputFigure = new LineFigure(this.outputFigureId, "Czas [s]", "Stężenie [%]");

    this.stateVariableFigureId = document.getElementById('plantPlot');
    this.stateVariableFigure = new LineFigure(this.stateVariableFigureId, "Czas [s]", "Poziom [cm]");

    this.productionFigureId = document.getElementById('productionPlot');
    this.productionFigure = new AreaFigure(this.productionFigureId, "Czas [s]", "Produkcja")

    this.concentrationLabelId = document.getElementById('concentrationLabel');
    this.concentrationLabel = new Label(this.concentrationLabelId, '%');

    this.currentProductionLabelId = document.getElementById('currentProduction');
    this.currentProductionLabel = new Label(this.currentProductionLabelId, "");
  }



}

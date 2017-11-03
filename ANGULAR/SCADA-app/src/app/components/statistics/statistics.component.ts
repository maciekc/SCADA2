import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import {Observable, Subscription} from "rxjs";

import { StateVariablesService } from '../../services/stateVariableService/state-variables.service';
import { StatisticService } from '../../services/statistic-service/statistic-service.service';
import { LineFigure, AreaFigure, BarFigure, Figure } from '../../class/figure';
import { Label } from '../../class/label';
import { Time } from '../../class/time';

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

  private productionBarFigure: Figure;
  private productionBarFigureId: HTMLElement;
  private productionLineFigure: Figure;
  private productionLineFigureId: HTMLElement;

  private concentrationLabelId: HTMLElement;
  private concentrationLabel: Label;
  private currentProductionLabelId: HTMLElement;
  private currentProductionLabel: Label;

  public time: Date;

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
    document.getElementById("ProductionDropButton").innerText = target.innerText;
    //BAR_HOUR
    //BAR_DAY
    if (id == "BAR_HOUR" || id == "BAR_DAY") {
      console.log("bar_mode")
      this.productionFigure = this.productionBarFigure;
    } else {
      this.productionFigure = this.productionLineFigure;
    }
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
      this.outputFigure.updatePlotData(dates, values, 'Stężenie');
    })
    this.serviceSubscriptions.push(output);

    let stateVariable = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.statisticService.getStateVariableDates();
      let values = this.statisticService.getStateVariableValues()
      this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]', 'rgb(255, 148, 0)');
    })
    this.serviceSubscriptions.push(stateVariable);

    let productionVariable = Observable.interval(5000)
    .subscribe(r => {
      let dates = this.statisticService.getProductionDates();
      let values = this.statisticService.getProductionValues();
      let currentLevel = values[values.length - 1];

      //tozeroy - niebieski
      //tonexty - czerwony
      console.log(values)
      this.productionFigure.updatePlotData(dates, values, 'Produkcja', 'tonexty');
      this.currentProductionLabel.setLabelValue(this.statisticService.getCurrentProduction());
    })
    this.serviceSubscriptions.push(productionVariable);

    let timeObs = Observable.interval(1000)
    .subscribe(r => {
      this.time = new Date();
    })
    this.serviceSubscriptions.push(timeObs);
    
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
    this.productionLineFigure = new AreaFigure(this.productionFigureId, "Czas [s]", "Produkcja");
    this.productionFigure = this.productionLineFigure;
    this.productionBarFigure = new BarFigure(this.productionFigureId, "Data", "Produkcja")

    this.concentrationLabelId = document.getElementById('concentrationLabel');
    this.concentrationLabel = new Label(this.concentrationLabelId, '%');

    this.currentProductionLabelId = document.getElementById('currentProduction');
    this.currentProductionLabel = new Label(this.currentProductionLabelId, "");
  }



}

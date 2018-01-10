import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import {Observable, Subscription} from "rxjs";

import { StateVariablesService } from '../../services/stateVariableService/state-variables.service';
import { StatisticService } from '../../services/statistic-service/statistic-service.service';
import { CommonService } from '../../services/common-service/common.service';
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
  private LEVEL_1: String = "";
  private LEVEL_2: String = "";
  private LEVEL_3: String = "";

  private refreshTime: number = 2000;
  private productionFigureMode = "LINE";

  // private currentConcentration: number = 0;
  // private currentLevel: number = 0;

  constructor(private statisticService: StatisticService, private commonDataService: CommonService) {}
  
  changeStateVariable(event) {
    let target = event.target;
    let id = target.id;
    document.getElementById("SVDropButton").innerText = target.innerText;
    this.statisticService.chanegStateVariablePlotTab(id)
    this.updateStateVariablePlotData()
  }

  changeProductionFigureMode(event) {
    let target = event.target;
    let id = target.id;
    this.productionFigureMode = id;
    document.getElementById("ProductionDropButton").innerText = target.innerText;
    //BAR_HOUR
    //BAR_DAY
    if (id == "BAR_HOUR" || id == "BAR_DAY") {
      this.productionFigure = this.productionBarFigure;
    } else {
      this.productionFigure = this.productionLineFigure;
    }
    this.statisticService.changeProductionFigureMode(id)
    this.updateProductionPlotData()
  }


  ngOnInit() {
    console.log("on init")
    this.initFigures()

    this.statisticService.startService();
    this.commonDataService.startService()
    
    this.updatePlotsData()

    this.updateIndicators()

    let output = Observable.interval(this.refreshTime)
    .subscribe(r => {
      let dates = this.statisticService.getOutputDates();
      let values = this.statisticService.getOutputValues();
      // this.concentrationLabel.setLabelValue(this.statisticService.getCurrentConcentration());
      this.outputFigure.updatePlotData(dates, values, 'Stężenie');
    })
    this.serviceSubscriptions.push(output);

    let stateVariable = Observable.interval(this.refreshTime)
    .subscribe(r => {
      let dates = this.statisticService.getStateVariableDates();
      let values = this.statisticService.getStateVariableValues()
      this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]', 'rgb(255, 148, 0)');
    })
    this.serviceSubscriptions.push(stateVariable);

    let productionVariable = Observable.interval(this.refreshTime)
    .subscribe(r => {
      let dates = this.statisticService.getProductionDates();
      let values = this.statisticService.getProductionValues();
      let currentLevel = values[values.length - 1];

      // if (this.productionFigureMode == "LINE") {
      //   console.log("len : " + values.length)
      //   for (let i=0; i < values.length; i++) {
      //     values[i] = values[i] / 10
      //   }
      // }
      console.log(values)
      //tozeroy - niebieski
      //tonexty - czerwony
      // console.log(values)
      this.productionFigure.updatePlotData(dates, values, 'Produkcja', 'tonexty');
      // this.currentProductionLabel.setLabelValue(this.statisticService.getCurrentProduction());
    })
    this.serviceSubscriptions.push(productionVariable);

    let currenValues = Observable.interval(this.refreshTime)
    .subscribe(r => {
      this.LEVEL_1 = this.commonDataService.getCurrentValue("LEVEL_1");
      this.LEVEL_2 = this.commonDataService.getCurrentValue("LEVEL_2");
      this.LEVEL_3 = this.commonDataService.getCurrentValue("LEVEL_3");
      let prod = this.commonDataService.getCurrentValue("VALVE_1")/10;
      let output = this.commonDataService.getCurrentValue("OUTPUT");
      
      this.concentrationLabel.setLabelValue(output.toString());
      this.currentProductionLabel.setLabelValue(prod.toPrecision(2));
    })
    this.serviceSubscriptions.push(currenValues);


    let timeObs = Observable.interval(1000)
    .subscribe(r => {
      this.time = new Date();
    })
    this.serviceSubscriptions.push(timeObs);
    
  }

  ngOnDestroy() {
    console.log("on destroy")
    this.statisticService.stopService();
    this.commonDataService.stopService();
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

  private updateIndicators() {
    this.commonDataService.initCurrentData()
    .then(r => {
      this.LEVEL_1 = this.commonDataService.getCurrentValue("LEVEL_1");
      this.LEVEL_2 = this.commonDataService.getCurrentValue("LEVEL_2");
      this.LEVEL_3 = this.commonDataService.getCurrentValue("LEVEL_3");
      let prod = this.commonDataService.getCurrentValue("VALVE_1")/10;
      let output = r.get("OUTPUT")[1].toPrecision(3);
      this.concentrationLabel.setLabelValue(output.toString());
      this.currentProductionLabel.setLabelValue(prod.toPrecision(2));

      this.time = new Date();
    })
  }

  private updateOutputPlotsData() {
    this.statisticService.initOutputPlotDataGetter()
    .then(r => {
      let dates = r.getDates();
      let values = r.getValues()
      this.outputFigure.updatePlotData(dates, values, 'wyjście');
    })
  }

  private updateStateVariablePlotData() {
    this.statisticService.initStateVariablePlotDataGetter()
    .then(r => {
      let dates = r.getDates();
      let values = r.getValues();
      this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]', 'orange');
    })
  }

  private updateProductionPlotData() {
    this.statisticService.initProductionPlotDataGetter()
    .then(r => {
      let dates = r.getDates();
      let values = r.getValues();
      this.productionFigure.updatePlotData(dates, values, 'Produkcja', 'tonexty');
    })
  }
  private updatePlotsData() {
    this.updateOutputPlotsData()
    this.updateStateVariablePlotData()  
    this.updateProductionPlotData()
  }



}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";

import { PlantService } from '../../services/plant-service/plant.service';
import { CommonService } from '../../services/common-service/common.service';
import { Figure, LineFigure, BarFigure, LineFigureThreeSeries } from '../../class/figure';
import { PlotData } from '../../class/plotDataGetter';

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
  private materialFigure: Figure;
  private materialFigureId: HTMLElement; 

  private level_1: number = 0;
  private level_2: number = 0;
  private level_3: number = 0;
  private output: number = 0;

  private limitMin: number = 0;
  private limitMinCrit: number = 0;
  private limitMax: number = 0;
  private limitMaxCrit: number = 0;
  
  private SVLimitMin: number = 0;
  private SVLimitMinCrit: number = 0;
  private SVLimitMax: number = 0;
  private SVLimitMaxCrit: number = 0;

  private limitTag: String = "LEVEL_1";
  private stateVariable: String = "LEVEL_1";

  private refreshTime: number = 2000;
  

  // private commonDAtaGetter: CommonService;

  constructor(private plantService: PlantService, private commonDataService: CommonService) {}
  
  changeLimitSubmit(event) {
    console.log(this.limitMin)
    let limitValues = [this.limitMin, this.limitMax, this.limitMinCrit, this.limitMaxCrit]
    this.plantService.changeLimitsValues(this.limitTag, this.commonDataService.getLimitsToLevel() , limitValues)
    .subscribe( v => {
      this.updateLimits(this.limitTag)
      if(document.getElementById("SVDropButton").id == this.limitTag) {
        this.changeStateVariableLimits(this.limitTag)
        .then(r => this.updateStateVariablePlotData());                
      }
      else {
        this.changeStateVariableLimits(this.limitTag);
      }
    })
  }

  changeLimits(event) {
    let target = event.target;
    this.limitTag = target.id;
    document.getElementById("LimitDropButton").innerText = target.innerText;
    this.updateLimits(this.limitTag)
  }

  changeStateVariable(event) {
    let target = event.target;
    this.stateVariable = target.id;
    document.getElementById("SVDropButton").innerText = target.innerText;
    this.plantService.chanegStateVariablePlotTab(this.stateVariable)

    this.changeStateVariableLimits(this.stateVariable)
    .then(r => this.updateStateVariablePlotData())
  }

  changeMaterialPlot(event) {
    let target = event.target;
    let id = target.id;
    document.getElementById("MaterialDropButton").innerText = target.innerText;
    this.plantService.changeMaterialFigureMode(id)
    this.updateMaterialPlotData()
  }

  ngOnInit() {
    console.log("on init")
    this.initFigures()

    this.plantService.startService();
    this.commonDataService.startService();

    this.updateLimits("LEVEL_1")
    this.updateIndicators()

    this.changeStateVariableLimits(this.stateVariable)
    .then(r => this.updatePlotsData())

    let output = Observable.interval(this.refreshTime)
    .subscribe(r => {
      let dates = this.plantService.getOutputDates();
      let values = this.plantService.getOutputValues()
      this.outputFigure.updatePlotData(dates, values, 'wyjście');
    })
    this.serviceSubscriptions.push(output);

    let stateVariable = Observable.interval(this.refreshTime)
    .subscribe(r => {
      let dates = this.plantService.getStateVariableDates();
      let values = this.plantService.getStateVariableValues()

      let upperLimitValues = [this.SVLimitMaxCrit, this.SVLimitMaxCrit]
      let upperLimitDates = [dates[0], dates[dates.length - 1]]
      let lowerLimitValues = [this.SVLimitMinCrit, this.SVLimitMinCrit]
      let lowerLimitDates = [dates[0], dates[dates.length - 1]]
      this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]', 'orange', lowerLimitDates, lowerLimitValues, upperLimitDates, upperLimitValues);

      // this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]', 'orange');
    })
    this.serviceSubscriptions.push(stateVariable);

    let materialVariable = Observable.interval(this.refreshTime)
    .subscribe(r => {
      let dates = this.plantService.getMaterialDates();
      let values = this.plantService.getMaterialValues()
      this.materialFigure.updatePlotData(dates, values, 'Ilość [m^3]', 'green');
    })
    this.serviceSubscriptions.push(materialVariable);

    let commonData = Observable.interval(this.refreshTime)
    .subscribe(r => {
      this.level_1 = this.commonDataService.getCurrentValue("LEVEL_1")
      this.level_2 = this.commonDataService.getCurrentValue("LEVEL_2")
      this.level_3 = this.commonDataService.getCurrentValue("LEVEL_3")
      this.output = this.commonDataService.getCurrentValue("OUTPUT")
    })
    this.serviceSubscriptions.push(commonData);

    
  }

  ngOnDestroy() {
    console.log("on destroy")
    this.plantService.stopService();
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
    this.stateVariableFigure = new LineFigureThreeSeries(this.stateVariableFigureId, "Czas [s]", "Poziom [cm]");

    this.materialFigureId = document.getElementById('materialPlot');;
    this.materialFigure = new BarFigure(this.materialFigureId, "Data", "Produkcja")
  }

  private updateLimits(tag: String) {
    
    this.commonDataService.initLimitsData(tag)
    .then(r => {
        console.log(r)
        let values = r.values();
        try {
          this.limitMin = values.next().value[0];
          this.limitMinCrit = values.next().value[0];
          this.limitMax = values.next().value[0];
          this.limitMaxCrit = values.next().value[0];
        } catch (e) {
          this.limitMin = 0;
          this.limitMinCrit = 0;
          this.limitMax = 0;
          this.limitMaxCrit = 0;
        }
      }
    )    
  }

  private changeStateVariableLimits(tag: String) {
    
    return this.commonDataService.initLimitsData(tag)
    .then(r => {
        console.log(r)
        let values = r.values();
        try {
          this.SVLimitMin = values.next().value[0];
          this.SVLimitMinCrit = values.next().value[0];
          this.SVLimitMax = values.next().value[0];
          this.SVLimitMaxCrit = values.next().value[0];
        } catch (e) {
          this.SVLimitMin = 0;
          this.SVLimitMinCrit = 0;
          this.SVLimitMax = 0;
          this.SVLimitMaxCrit = 0;
        }
      }
    )  
  }

  private updateIndicators() {
    this.commonDataService.initCurrentData()
    .then(r => {
      this.level_1 = r.get("LEVEL_1")[1].toPrecision(3);
      this.level_2 = r.get("LEVEL_2")[1].toPrecision(3);
      this.level_3 = r.get("LEVEL_3")[1].toPrecision(3);
      this.output = r.get("OUTPUT") [1].toPrecision(3);
    })
  }

  private updateMaterialPlotData() {
    this.plantService.initMaterialPlotDataGetter()
    .then(r => {
      let dates = r.getDates();
      let values = r.getValues()
      this.materialFigure.updatePlotData(dates, values, 'wyjście');
    })
  }

  private updateOutputPlotData() {
    this.plantService.initOutputPlotDataGetter()
    .then(r => {
      let dates = r.getDates();
      let values = r.getValues()
      this.outputFigure.updatePlotData(dates, values, 'wyjście');
    })
  }

  private updateStateVariablePlotData() {
    this.plantService.initStateVariablePlotDataGetter()
    .then(r => {
      let dates = r.getDates();
      let values = r.getValues();
      let upperLimitValues = [this.SVLimitMaxCrit, this.SVLimitMaxCrit]
      let upperLimitDates = [dates[0], dates[dates.length - 1]]
      let lowerLimitValues = [this.SVLimitMinCrit, this.SVLimitMinCrit]
      let lowerLimitDates = [dates[0], dates[dates.length - 1]]
      this.stateVariableFigure.updatePlotData(dates, values, 'Poziom [cm]', 'orange', lowerLimitDates, lowerLimitValues, upperLimitDates, upperLimitValues);
    })
  }
  private updatePlotsData() {
    this.updateOutputPlotData()
    this.updateStateVariablePlotData()
    this.updateMaterialPlotData()
  }
}

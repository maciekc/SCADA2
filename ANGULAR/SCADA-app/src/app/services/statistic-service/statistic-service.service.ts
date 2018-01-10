import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";


import { PlotDataGetter } from '../../class/plotDataGetter';


@Injectable()
export class StatisticService {

  private subscriptions: Subscription [] = [];
  private outputPlotDataGetter: PlotDataGetter;
  private controllPlotDataGetter: PlotDataGetter;
  private stateVariablePlotDataGetter: PlotDataGetter;
  private productionPlotDataGetter: PlotDataGetter;
  

  constructor(private http: HttpClient) {
    this.outputPlotDataGetter = new PlotDataGetter(http, "OUTPUT");
    this.controllPlotDataGetter = new PlotDataGetter(this.http, "VALVE_1");
    this.stateVariablePlotDataGetter = new PlotDataGetter(this.http, "LEVEL_1");
    this.productionPlotDataGetter = new PlotDataGetter(this.http, "VALVE_1");
  }

  public chanegControllPlotTab(tag: String) {
    this.controllPlotDataGetter.setTag(tag);
  }

  public chanegStateVariablePlotTab(tag: String) {
    this.stateVariablePlotDataGetter.setTag(tag);
  }

  public changeProductionFigureMode(mode: String) {
    this.productionPlotDataGetter.setMode(mode);
  }

  public initControllerDataGetter(tag: String ) {
    this.controllPlotDataGetter = new PlotDataGetter(this.http, tag);
  }

  public getControllerValues() {
    return this.controllPlotDataGetter.getValues()
  }

  public getControllerDates() {
    return this.controllPlotDataGetter.getDates()
  }
  
  public getOutputValues() {
    return this.outputPlotDataGetter.getValues()
  }
  
  public getOutputDates() {
    return this.outputPlotDataGetter.getDates()
  }

  public getStateVariableValues() {
    return this.stateVariablePlotDataGetter.getValues()
  }
  
  public getStateVariableDates() {
    return this.stateVariablePlotDataGetter.getDates()
  }

  public getProductionValues() {
    let values = this.productionPlotDataGetter.getValues()
    return values
  }
  
  public getProductionDates() {
    return this.productionPlotDataGetter.getDates()
  }

  public getCurrentConcentration() {
    let v = this.outputPlotDataGetter.getValues()
    if (v.length > 0) {
      return v[v.length - 1].toPrecision(2)
    }
    else {
      return "0"
    }
  }

  public getCurrentProduction() {
    let v = this.productionPlotDataGetter.getValues()
    if (v.length > 0) {
      return v[v.length - 1].toPrecision(2)
    }
    else {
      return "0"
    }
  }
  
  public startService() {
    let sub = this.controllPlotDataGetter.plotGetter();
    this.subscriptions.push(sub);
    let sub2 = this.outputPlotDataGetter.plotGetter();
    this.subscriptions.push(sub2);
    let sub3 = this.stateVariablePlotDataGetter.plotGetter()
    this.subscriptions.push(sub3);
    let sub4 = this.productionPlotDataGetter.plotGetter()
    this.subscriptions.push(sub4);
  }

  public stopService() {
    this.subscriptions.forEach(s => s.unsubscribe())
    this.subscriptions = [];
  }


  public initOutputPlotDataGetter() {
    return this.outputPlotDataGetter.initPlotDataGetter()
  }

  public initControllPlotDataGetter() {
    return this.controllPlotDataGetter.initPlotDataGetter()
  }
  
  public initStateVariablePlotDataGetter() {
    return this.stateVariablePlotDataGetter.initPlotDataGetter()
  }

  public initProductionPlotDataGetter() {
    return this.productionPlotDataGetter.initPlotDataGetter()
  }

}

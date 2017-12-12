import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";


import { PlotDataGetter } from '../../class/plotDataGetter';


@Injectable()
export class ControllerService {

  private subscriptions: Subscription [] = [];
  private controllPlotDataGetter: PlotDataGetter;
  private stateVariablePlotDataGetter: PlotDataGetter;
  

  constructor(private http: HttpClient) {
    this.controllPlotDataGetter = new PlotDataGetter(this.http, "VALVE_1");
    this.stateVariablePlotDataGetter = new PlotDataGetter(this.http, "LEVEL_1");
  }

  public chanegControllPlotTab(tag: String) {
    this.controllPlotDataGetter.setTag(tag);
  }

  public chanegStateVariablePlotTab(tag: String) {
    this.stateVariablePlotDataGetter.setTag(tag);
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

  public getStateVariableValues() {
    return this.stateVariablePlotDataGetter.getValues()
  }
  
  public getStateVariableDates() {
    return this.stateVariablePlotDataGetter.getDates()
  }
  
  public startService() {
    let sub = this.controllPlotDataGetter.plotGetter();
    this.subscriptions.push(sub);

    let sub3 = this.stateVariablePlotDataGetter.plotGetter()
    this.subscriptions.push(sub3)
  }

  public stopService() {
    this.subscriptions.forEach(s => s.unsubscribe())
    this.subscriptions = [];
  }


  public initControllPlotDataGetter() {
    return this.controllPlotDataGetter.initPlotDataGetter()
  }
  
  public initStateVariablePlotDataGetter() {
    return this.stateVariablePlotDataGetter.initPlotDataGetter()
  }

}


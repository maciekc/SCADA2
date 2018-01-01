import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";


import { PlotDataGetter } from '../../class/plotDataGetter';
import { SendDataToServer } from '../../class/sendDataToServer';
import { ControllerDataGetter } from '../../class/controllerDataGetter';



@Injectable()
export class ControllerService {

  private subscriptions: Subscription [] = [];
  private controllPlotDataGetter: PlotDataGetter;
  private stateVariablePlotDataGetter: PlotDataGetter;
  private controllerDataGetter: ControllerDataGetter;
  private sendDataToServer : SendDataToServer;
  

  constructor(private http: HttpClient) {
    this.controllPlotDataGetter = new PlotDataGetter(this.http, "VALVE_1");
    this.stateVariablePlotDataGetter = new PlotDataGetter(this.http, "LEVEL_1");
    this.controllerDataGetter = new ControllerDataGetter(http);
    this.sendDataToServer = new SendDataToServer(http);
  }

  public chanegControllPlotTab(tag: String) {
    this.controllPlotDataGetter.setTag(tag);
  }

  public chanegStateVariablePlotTab(tag: String) {
    this.stateVariablePlotDataGetter.setTag(tag);
  }


  public initControllerDataGetter() {
    this.controllerDataGetter.initControllerDataGetter();
  }

  public initControllerParameters() {
    return this.controllerDataGetter.initControllerParametersData();
  }

  public getControllersDataVaules() {
    return this.controllerDataGetter.getControllersValues()
  }

  public getControllersParametersValues() {
    return this.controllerDataGetter.getControllersParametersValues()
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
    
    // let subC = this.controllerDataGetter.controllerDataGetter()
    // this.subscriptions.push(subC)
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

  public changeControllersParametersValues(tags: String[], values: number[]) {
    return this.sendDataToServer.sendControllerParametersToServer(tags, values)
  }

}


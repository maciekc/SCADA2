import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";


import { PlotDataGetter } from '../../class/plotDataGetter';
import { SendDataToServer } from '../../class/sendValuesToServer';


@Injectable()
export class PlantService {

  private subscriptions: Subscription [] = [];
  private outputPlotDataGetter: PlotDataGetter;
  // private controllPlotDataGetter: PlotDataGetter;
  private stateVariablePlotDataGetter: PlotDataGetter;
  private sendDataToServer : SendDataToServer;
  

  constructor(private http: HttpClient) {
    this.outputPlotDataGetter = new PlotDataGetter(http, "OUTPUT");
    // this.controllPlotDataGetter = new PlotDataGetter(this.http, "VALVE_1");
    this.stateVariablePlotDataGetter = new PlotDataGetter(this.http, "LEVEL_1");
    this.sendDataToServer = new SendDataToServer(http);
  }


  public chanegStateVariablePlotTab(tag: String) {
    this.stateVariablePlotDataGetter.setTag(tag);
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
  
  public startService() {
    let sub2 = this.outputPlotDataGetter.plotGetter();
    this.subscriptions.push(sub2);
    let sub3 = this.stateVariablePlotDataGetter.plotGetter()
    this.subscriptions.push(sub3)
  }

  public stopService() {
    this.subscriptions.forEach(s => s.unsubscribe())
    this.subscriptions = [];
  }

  public changeLimitsValues(limitTag: String, limitMin: number, limitMax: number, limitMinCrit: number, limitMaxCrit: number) {
    this.sendDataToServer.sendLimitsToServer(limitTag, limitMin, limitMax, limitMinCrit, limitMaxCrit)
  }



}

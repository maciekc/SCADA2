import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";


import { CommonDataGetter } from '../../class/commonDataGetter';

@Injectable()
export class CommonService {

  private subscriptions: Subscription [] = [];
  private commonDataGetter: CommonDataGetter;

  constructor(private http: HttpClient) {
    this.commonDataGetter = new CommonDataGetter(this.http);
  }

  public startService() {
    let sub = this.commonDataGetter.currentDataGetter();
    this.subscriptions.push(sub);
  }

  public stopService() {
    this.subscriptions.forEach(s => s.unsubscribe())
    this.subscriptions = [];
  }

  public getCurrentValue(tag: String) {
    
    try {
     return this.commonDataGetter.getData().get(tag)[1].toPrecision(2);
    } 
    catch (e) {
      return "0"
    }
  }

  public initLimitsData(tag: String) {
    return this.commonDataGetter.limitsDataGetter(tag);
  }

  public initCurrentData() {
    return this.commonDataGetter.initCommonDataGetter()
  }

  public getLimitsData() {
    return this.commonDataGetter.getLimitsData()
  }

  public getStateVariableData() {
    return this.commonDataGetter.getStateVariableData()
  }
}

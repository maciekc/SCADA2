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
    let sub = this.commonDataGetter.dataGetter();
    this.subscriptions.push(sub);
  }

  public stopService() {
    this.subscriptions.forEach(s => s.unsubscribe())
    this.subscriptions = [];
  }

  public getCurrentValue(tag: String) {
    return this.commonDataGetter.getData().get(tag)
  }

  public getLimitData(tag: String) {
    return this.commonDataGetter.getLimitData(tag);
  }
}

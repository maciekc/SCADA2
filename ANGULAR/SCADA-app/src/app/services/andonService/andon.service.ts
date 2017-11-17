import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";
import { AndonDataGetter } from '../../class/andonDataGetter';

@Injectable()
export class AndonService {

  private subscriptions: Subscription [] = [];
  private andonDataGetter: AndonDataGetter;

  constructor(private http: HttpClient) {
    this.andonDataGetter = new AndonDataGetter(this.http);
  }

  public startService() {
    let sub = this.andonDataGetter.andonGetter();
    this.subscriptions.push(sub);
  }

  public stopService() {
    this.subscriptions.forEach(s => s.unsubscribe())
    this.subscriptions = [];
  }

  public getAndonData() {
    return this.andonDataGetter.getAndonData()
  }

  public initAndonData() {
    return this.andonDataGetter.initAndonDataGetter()
  }

}

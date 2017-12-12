import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import {Observable, Subscription} from "rxjs";
import {Point} from '../../class/point';
// import 'rxjs/*';

@Injectable()
export class StateVariablesService implements OnInit {

  url = "http://localhost:8010/stateVariableData";

  OUTPUTDates: String[] = [];
  OUTPUTValues: number[] = [];

  data: Point[];

  statisticsSubscription: Subscription [] = [];
  controllerSubscription: Subscription [] = [];
  plantSubscription: Subscription [] = [];

  constructor(private http: HttpClient) {}
 
  public getOUTPUTDates(): String[] {
    console.log(this.OUTPUTDates)
    return this.OUTPUTDates;
  }

  public getOUTPUTValues(): number[] {
    console.log(this.OUTPUTValues)
    return this.OUTPUTValues;
  }


  public outputPlotGetter() {
    let outputSub: Subscription = Observable.interval(5000) 
    .subscribe(v => {
      let params = new HttpParams().set('name', 'OUTPUT');
      let dates: String[] = [];
      let values: number[] = [];
      
      this.http.get<Point []>(this.url, {params})
      .forEach(r => {
        r.forEach(e => {
          dates.push(e["date"]);
          values.push(e["value"]);
        })
        this.OUTPUTDates = dates;
        this.OUTPUTValues = values;
        })
        
    });

    this.statisticsSubscription.push(outputSub);
  }

  public disposeStatisticsSubscriptions() {
    this.statisticsSubscription.forEach(e => e.unsubscribe())
    this.statisticsSubscription = [];
  }

  public disposeControllerSubscriptions() {
    this.controllerSubscription.forEach(e => e.unsubscribe())
    this.controllerSubscription = [];
  }

  public disposePlantSubscriptions() {
    this.plantSubscription.forEach(e => e.unsubscribe())
    this.plantSubscription = [];
  }

  ngOnInit() {
  //   Observable.interval(5000)
  //   .subscribe(v => {
  //     console.log("data service start");
  //     let params = new HttpParams().set('name', 'LEVEL_3');
  //     this.http.get(this.url, {params})
  //     .subscribe(res => console.log("response " + res))
  //   })
  }
}

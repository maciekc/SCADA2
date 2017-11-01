import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {Point} from './point';


export class PlotDataGetter {

    private dates: String[] = [];
    private values: number[] = [];
    private url: string = "http://localhost:8010/stateVariableData";

    constructor(private http: HttpClient, private tag: String) {}

    public setTag(newTag : String) {
        this.tag = newTag;
    }
    public getDates(): String[] {
        return this.dates;
    }
    
    public getValues(): number[] {
    return this.values;
    }

    public plotGetter(): Subscription {
    let subscription: Subscription = Observable.interval(5000) 
    .subscribe(v => {
        let params = new HttpParams().set('name', this.tag.toString());
        let dates: String[] = [];
        let values: number[] = [];
        
        this.http.get<Point []>(this.url, {params} )
        .forEach(r => {
        r.forEach(e => {
            dates.push(e["date"]);
            values.push(e["value"]);
        })
        this.dates = dates;
        this.values = values;
        })      
    });
    return subscription;
    }

}
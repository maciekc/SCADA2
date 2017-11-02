import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {Point} from './point';


export class PlotDataGetter {

    private dates: String[] = [];
    private values: number[] = [];
    private url: string = "http://localhost:8010/stateVariableData";
    private mode: String = "LINE"

    constructor(private http: HttpClient, private tag: String) {}

    public setMode(mode: String) {
        this.mode = mode;
    }
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
        if (this.mode != "LINE") {
            params.append('mode', this.mode.toString());
        } 
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
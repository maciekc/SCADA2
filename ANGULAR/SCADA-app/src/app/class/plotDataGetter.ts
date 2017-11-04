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
        if (mode != "LINE") {
            this.url = "http://localhost:8010/stateVariableData/" + mode
        } else {
            this.url = this.url = "http://localhost:8010/stateVariableData"
        }
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
            this.getCurrentServerPlotData()
    });
    return subscription;
    }

    public initPlotDataGetter() {
        return this.getCurrentServerPlotData().then(v => new PlotData(this.dates, this.values))
    }

    private getCurrentServerPlotData() {
        let params = new HttpParams().set('name', this.tag.toString());
        
        let dates: String[] = [];
        let values: number[] = [];
        
        return this.http.get<Point []>(this.url, {params} )
        .forEach(r => {
        r.forEach(e => {
            dates.push(e["date"]);
            values.push(e["value"]);
        })
        this.dates = dates;
        this.values = values;
        })
    }

}

export class PlotData {
    constructor(private dates: String[],private values: number []) {}
    public getDates() {
        return this.dates;
    }

    public getValues() {
        return this.values;
    }
}
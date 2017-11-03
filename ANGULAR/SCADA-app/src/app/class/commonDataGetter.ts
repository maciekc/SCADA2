import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {Point} from './point';

export class CommonDataGetter {

    private data: Map<String, Object>;
    
    private tags: String[] = ["LEVEL_1", "LEVEL_2", "LEVEL_3", "VALVE_1", "VALVE_2", "VALVE_3", "VALVE_4", "OUTPUT"]
    private url: string = "http://localhost:8010/currentData";

    constructor(private http: HttpClient) {}

    public dataGetter(): Subscription {
        let subscription: Subscription = Observable.interval(5000) 
        .subscribe(v => {
           
            let dates: String[] = [];
            let values: number[] = [];
            let index = 0;
            
            this.http.get<Point []>(this.url)
            .forEach(r => {
                r.forEach(e => {
                    dates.push(e["date"]);
                    values.push(e["value"]);
                    this.data.set(this.tags[index], [dates[index], values[index]])
                    index += 1;
                })
            })      
        });
        return subscription;
    }

    public getData() {
        return this.data;
    }

    //TODO
    public getLimitData(tag: String) {
        return ""
    }

    
}
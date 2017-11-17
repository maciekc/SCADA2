import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {Point} from './point';
import { Andon } from "./andon";

export class AndonDataGetter {

    private Andons: Andon[] = [];
    
    private tags: String[] = ["LEVEL_1", "LEVEL_2", "LEVEL_3", "VALVE_1", "VALVE_2", "VALVE_3", "VALVE_4", "OUTPUT"]
    private urlAndonData: string = "http://localhost:8010/requestAndon";
    
    constructor(private http: HttpClient) {}

    public getAndonData() {
        return this.Andons;
    }

    public andonGetter(): Subscription {
        let subscription: Subscription = Observable.interval(1000) 
        .subscribe(v => {
                this.getCurrentAndonData()
        });
        return subscription;
    }

    public initAndonDataGetter() {
        return this.getCurrentAndonData(-1).then(v => this.Andons)
    }

    private getCurrentAndonData(startId: Number = 0) {
        
        let andons: Andon[] = [];
        let params = new HttpParams().set('startIndex', startId.toString());
        return this.http.get<Andon []>(this.urlAndonData, {params})
        .forEach(r => {
        r.forEach(e => {
            let rec: Andon = new Andon(e["limitTag"], e["stateSpaceTag"], e["date"], e["value"], e["type"])
            andons.push(rec);
        })
        this.Andons = andons;
        })
    }


}


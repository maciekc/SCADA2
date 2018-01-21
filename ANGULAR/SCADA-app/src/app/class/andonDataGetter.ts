import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {Point} from './point';
import { Andon } from "./andon";
import { Notification } from "rxjs/Notification";
import { interval } from "rxjs/observable/interval";

export class AndonDataGetter {

    private Andons: Andon[] = [];
    
    private tags: String[] = ["LEVEL_1", "LEVEL_2", "LEVEL_3", "VALVE_1", "VALVE_2", "VALVE_3", "VALVE_4", "OUTPUT"]
    // private urlAndonData: string = "http://localhost:8010/requestAndon";
    private urlAndonData: string = "http://192.168.1.100:8010/requestAndon";

    private andonGetterClass: NotificationData = new NotificationData(this.http, this.urlAndonData);
   
    
    constructor(private http: HttpClient) {}

    //--------------------------------------------------------
    //                  Andon data
    //--------------------------------------------------------
    public getAndonData() {
        return this.andonGetterClass.getData();
    }

    public andonGetter(): Subscription {
        return this.andonGetterClass.intervalDataGetter();
    }

    public initAndonDataGetter() {
        return this.andonGetterClass.initDataGetter()
    }

    private getCurrentAndonData(startId: Number = 0) {     
        this.andonGetterClass.getCurrentData()
    }
}
class NotificationData {

    constructor(protected http: HttpClient, protected url: String, protected data: Andon[] = []) {}

    public getData() {
        return this.data;
    }

    public intervalDataGetter(interval: number = 1000): Subscription {
        let subscription: Subscription = Observable.interval(interval) 
        .subscribe(v => {
                this.getCurrentData()
        });
        return subscription;
    }

    public initDataGetter() {
        return this.getCurrentData(-1).then(v => this.data)
    }

    public getCurrentData(startId: Number = 0) {
        
        let andons: Andon[] = [];
        let params = new HttpParams().set('startIndex', startId.toString());
        return this.http.get<Andon []>(this.url.toString(), {params})
        .forEach(r => {
        r.forEach(e => {
            let rec: Andon = new Andon(e["limitTag"], e["stateSpaceTag"], e["date"], e["value"], e["type"])
            andons.push(rec);
        })
        this.data = andons;
        })
    }
}


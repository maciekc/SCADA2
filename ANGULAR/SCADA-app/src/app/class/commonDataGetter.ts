import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {Point} from './point';
import {StateVariableData} from './stateVariableData';

export class CommonDataGetter {

    private currentData: Map<String, Object> = new Map();
    private limitsValues: Map<String, Object> = new Map();
    
    private tags: String[] = ["LEVEL_1", "LEVEL_2", "LEVEL_3", "VALVE_1", "VALVE_2", "VALVE_3", "VALVE_4", "OUTPUT"]
    private urlCurrentData: string = "http://localhost:8010/currentData";
    private urlLimitData: string = "http://localhost:8010/limitsData";

    constructor(private http: HttpClient) {}

    public currentDataGetter(): Subscription {
        let subscription: Subscription = Observable.interval(5000) 
        .subscribe(v => {
           this.getCurrentServerData()      
        });
        return subscription;
    }

    public getData(): Map<String, Object> {
        return this.currentData;
    }

    //TODO
    public limitsDataGetter(tag: String) {

        console.log("getter " + tag)
        let params = new HttpParams().set('stateVariableTag', tag.toString());
        return this.http.get<StateVariableData []>(this.urlLimitData, {params})
            .forEach(r => {
                let type: number = 0;
                let value: number = 0;
                let tag: String = "";
                let name: String = "";
                let limitsValues: Map<String, Object> = new Map();
                r.forEach(e => {
                    value = e["value"];
                    tag = e["tag"];
                    name = e["name"];
                    type = e["type"];
                    limitsValues.set(tag, [value, name, type]);

                            
                })
                this.limitsValues = limitsValues;
            })
            .then(v => this.limitsValues)        
    }

    public initCommonDataGetter() {
        return this.getCurrentServerData().then(v => this.currentData)
    }

    private getCurrentServerData() {
        let date: String = "";
        let value: number = 0;
        let tag: String = "";
                   
        return this.http.get<StateVariableData []>(this.urlCurrentData)
        .forEach(r => {
            r.forEach(e => {
                date = e["date"];
                value = e["value"];
                tag = e["stateSpaceTag"];
                this.currentData.set(tag, [date, value])               
            })
        })
    }

    
}
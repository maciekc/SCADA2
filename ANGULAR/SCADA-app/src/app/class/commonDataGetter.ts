import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {Point} from './point';
import {StateVariableData} from './stateVariableData';

export class CommonDataGetter {

    private currentData: Map<String, Object> = new Map();
    private limitsValues: Map<String, Object> = new Map();
    private limitsData: Map<String, String> = new Map();
    private stateVariableData: Map<String, String> = new Map();
    
    private tags: String[] = ["LEVEL_1", "LEVEL_2", "LEVEL_3", "VALVE_1", "VALVE_2", "VALVE_3", "VALVE_4", "OUTPUT"]
    private urlCurrentData: string = "http://localhost:8010/currentData";
    private urlLimitData: string = "http://localhost:8010/limitsData";

    constructor(private http: HttpClient) {
        this.initLimitsData()
        this.initStateVariableData()
    }

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

    private initLimitsData() {
        for (let i =0; i< 3; i++) {
            this.limitsData.set("LEVEL_" + (i+1) + "_MIN", "Poziom min")
            this.limitsData.set("LEVEL_" + (i+1) + "_MAX", "Poziom max")
            this.limitsData.set("LEVEL_" + (i+1) + "_MIN_CRITICAL", "Poziom min kryt.")
            this.limitsData.set("LEVEL_" + (i+1) + "_MAX_CRITICAL", "Poziom max kryt.")
        }
    }

    public getLimitsData() {
        return this.limitsData
    }

    public initStateVariableData() {
        for (let i =0; i< 3; i++) {
            this.stateVariableData.set("LEVEL_" + (i+1), "Zbiornik " + (i+1))
        }
        for (let i =0; i< 4; i++) {
            this.stateVariableData.set("VALVE_" + (i+1), "Zawór " + (i+1))
        }
        this.stateVariableData.set("OUTPUT", "Wyjście")
        this.stateVariableData.set("SET_POINT", "Wart. zadana")
    }
    public getStateVariableData() {
        return this.stateVariableData
    }

    
}
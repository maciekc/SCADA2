import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {Point} from './point';
import {ControllerData} from './controllerData';

export class ControllerDataGetter {

    private currentData: Map<String, Object> = new Map();
    private parametersData: Map<String, Object> = new Map();
    
    private tags: String[] = ["LEVEL_1", "LEVEL_2", "LEVEL_3", "VALVE_1", "VALVE_2", "VALVE_3", "VALVE_4", "OUTPUT"]
    private urlCurrentData: string = "http://localhost:8010/controllerData";
    private urlParametersData: string = "http://localhost:8010/controllersParameters";

    constructor(private http: HttpClient) {
        // this.initControllerDataGetter()
        this.initControllerParametersData()
    }


    public getControllersData(): Map<String, Object> {
        return this.currentData;
    }

    public controllerDataGetter(): Subscription {
        let subscription: Subscription = Observable.interval(5000) 
        .subscribe(v => {
                this.getCurrentControllerServerData()
        });
        return subscription;
    }

    //TODO
    public getControllersParametertsServerData() {

        return this.http.get<ControllerData []>(this.urlParametersData)
            .forEach(r => {
                let value: number = 0;
                let tag: String = "";
                let name: String = "";
                let parametersValues: Map<String, Object> = new Map();
                r.forEach(e => {
                    value = e["value"];
                    tag = e["tag"];
                    name = "";
                    parametersValues.set(tag, [value, name]);                  
                })
                this.parametersData = parametersValues;
            })
            .then(v => this.parametersData)        
    }

    public initControllerDataGetter() {
        return this.getCurrentControllerServerData()
    }

    private getCurrentControllerServerData() {
        
        let value: number = 0;
        let tag: String = "";
        let name: String = "";           
        return this.http.get<ControllerData []>(this.urlCurrentData)
        .forEach(r => {
            r.forEach(e => {
                value = e["value"];
                tag = e["tag"];
                name = e["name"];
                this.currentData.set(tag, [value, name])               
            })
        })
        .then(v => this.currentData)
    }

    public initControllerParametersData() {
        return this.getControllersParametertsServerData()
    }

    public getControllersParametersData() {
        return this.parametersData
    }

    public getControllersValues() {
        let values: number[] = [];
        this.currentData.forEach((v, k) => values.push(v[0]))
        return values
    }

    public getControllersParametersValues() {
        let values: number[] = []
        this.parametersData.forEach((v, k) => values.push(v[0]))
        return values
    }
    
}
import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

export class Andon {

    private currentData: Map<String, Object> = new Map();
    private limitsValues: Map<String, Object> = new Map();
    
    private tags: String[] = ["LEVEL_1", "LEVEL_2", "LEVEL_3", "VALVE_1", "VALVE_2", "VALVE_3", "VALVE_4", "OUTPUT"]
    
    constructor(private limitTag: String, private stateVariableTag: String, private date: String, private value: number, private type: number) {}
    public getDate() {
        return this.date.substr(0,19);
    }
    public getValue() {
        return this.value;
    }
    public getLimitTag() {
        return this.limitTag;
    }
    public getStateVariableTag() {
        return this.stateVariableTag;
    }
    public getType() {
        return this.type;
    }
}
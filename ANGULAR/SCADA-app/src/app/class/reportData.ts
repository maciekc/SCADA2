import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

export class ReportData {

    constructor(private stateVariableTag: String, private date: String, private value: number,private id: number, private type: number = -1, private reasonTag: String = "") {}
    public getDate() {
        return this.date.substr(0,19);
    }
    public getValue() {
        return this.value.toPrecision(4);
    }
    public getReasonTag() {
        return this.reasonTag;
    }
    public getStateVariableTag() {
        return this.stateVariableTag;
    }
    public getType() {
        return this.type;
    }
    public getID() {
        return this.id;
    }
}
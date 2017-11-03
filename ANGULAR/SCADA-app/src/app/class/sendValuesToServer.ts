import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {Point} from './point';

export class SendDataToServer {

    private url: string = "http://localhost:8010/changeLimits";
    
    constructor(private http: HttpClient) {}

    //TODO
    public sendLimitsToServer(limitTag: String, limitMin: number, limitMax: number, limitMinCrit: number, limitMaxCrit: number) {
        return;
    }
}
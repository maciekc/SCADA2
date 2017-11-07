import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Response, RequestMethod, RequestOptions, Headers } from '@angular/http'
import {Point} from './point';

export class SendDataToServer {

    private url: string = "http://localhost:8010/changeLimits";
    
    constructor(private http: HttpClient) {}

    //TODO
    public sendLimitsToServer(limitTag: String, limitMin: number, limitMax: number, limitMinCrit: number, limitMaxCrit: number) {

        let y = new LimitUpdate("LEVEL_1", 23.3);
        let re = new HttpHeaders({'Content-Type': 'application/json'})

        re.append('Accept', 'text/plain; charset=UTF-8');
        re.append('Accept', 'application/json');
        
        let ww = JSON.stringify(y);

        const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
        console.log(JSON.stringify(y))
        this.http
            .post('http://localhost:8010/limits', ww, {headers: re, responseType:'json'})
            .subscribe(v => console.log("send req 3 json"))
            this.http

        return;
    }
}

class LimitUpdate {
    constructor(private tag: String, private value: number){}
}
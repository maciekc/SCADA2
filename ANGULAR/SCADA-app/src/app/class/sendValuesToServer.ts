import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Response, RequestMethod, RequestOptions, Headers } from '@angular/http'
import {Point} from './point';

export class SendDataToServer {

    private url: string = "http://localhost:8010/changeLimits";
    
    constructor(private http: HttpClient) {}

    //TODO
    public sendLimitsToServer(limitTag: String, limitMin: number, limitMax: number, limitMinCrit: number, limitMaxCrit: number) {

        let header = new HttpHeaders({'Content-Type': 'application/json'})
        header.append('Accept', 'text/plain; charset=UTF-8');
        header.append('Accept', 'application/json');

        let minlimit = new LimitUpdate(limitTag, limitMin, -1);
        let minCritlimit = new LimitUpdate(limitTag, limitMinCrit, -2);
        let maxlimit = new LimitUpdate(limitTag, limitMax, 1);
        let maxCritlimit = new LimitUpdate(limitTag, limitMaxCrit, 2);
        
        let minlimitJSON = JSON.stringify(minlimit);
        let minCritlimitJSON = JSON.stringify(minCritlimit)
        let maxlimitJSON = JSON.stringify(maxlimit)
        let maxCritlimitJSON = JSON.stringify(maxCritlimit)

        let minObs = this.http
            .post('http://localhost:8010/limits', minlimitJSON, {headers: header, responseType:'json'})
        let minCritObs = this.http
            .post('http://localhost:8010/limits', minCritlimitJSON, {headers: header, responseType:'json'})
        let maxObs = this.http
            .post('http://localhost:8010/limits', maxlimitJSON, {headers: header, responseType:'json'})
        let maxCritObs = this.http
            .post('http://localhost:8010/limits', maxCritlimitJSON, {headers: header, responseType:'json'})

        return Observable.zip(minObs, minCritObs, maxObs, maxCritObs)
        // Observable.concat(minObs, minCritObs, maxObs, maxCritObs)
        //     .subscribe(v => console.log("send all of limits to update in DB."))


        // return;
    }
}

class LimitUpdate {
    constructor(private tag: String, private value: number, private type: number){}
}
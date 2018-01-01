import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Response, RequestMethod, RequestOptions, Headers } from '@angular/http'
import {Point} from './point';

export class SendDataToServer {

    private url: string = "http://localhost:8010/changeLimits";
    private static limitsToLevelValues: Map<String, number[]> = new Map();
    constructor(private http: HttpClient) {
        SendDataToServer.limitsToLevelValues.set("LEVEL_1", [0,0,0,0])
        SendDataToServer.limitsToLevelValues.set("LEVEL_2", [0,0,0,0])
        SendDataToServer.limitsToLevelValues.set("LEVEL_3", [0,0,0,0])
    }

    //TODO
    public sendLimitsToServer(levelTag: String, limitsToLevel: Map<String, String[]>, values: number[]) {

        let header = new HttpHeaders({'Content-Type': 'application/json'})
        header.append('Accept', 'text/plain; charset=UTF-8');
        header.append('Accept', 'application/json');

        let requestObs: Subscription[] = [];
        let prevLimitsValues: number[] = SendDataToServer.limitsToLevelValues.get(levelTag)
        let limitsTags = limitsToLevel.get(levelTag)
        for(let i = 0; i< 4; i++) {
            if (prevLimitsValues[i] != values[i]) {
                let limitJSON = JSON.stringify(new LimitUpdate(limitsTags[i], Number(values[i]),0))
                console.log(limitJSON)
                console.log(Number(values[i]))
                let req = this.http.post('http://localhost:8010/limits', limitJSON, {headers: header, responseType:'json'})
                .subscribe(r=> console.log("send "))
                requestObs.push(req)
            }
            values[i] = Number(values[i])
        }
        console.log(SendDataToServer.limitsToLevelValues)
        SendDataToServer.limitsToLevelValues.set(levelTag, values)
        
        return Observable.zip(requestObs)
    }

    public sendControllerParametersToServer(tags: String[], values: number[]) {
        
                let header = new HttpHeaders({'Content-Type': 'application/json'})
                header.append('Accept', 'text/plain; charset=UTF-8');
                header.append('Accept', 'application/json');
        
                let requestObs: Subscription[] = [];
                
                for(let i = 0; i< 4; i++) {
                        let limitJSON = JSON.stringify(new ControllerParameterUpdate(tags[i], Number(values[i])))
                        console.log(limitJSON)
                        console.log(Number(values[i]))
                        let req = this.http.post('http://localhost:8010/controllerParameter', limitJSON, {headers: header, responseType:'json'})
                        .subscribe(r=> console.log("send "))
                        requestObs.push(req)
                }                
                return Observable.zip(requestObs)
    }
}

class LimitUpdate {
    constructor(private tag: String, private value: number, private type: number){}
}

class ControllerParameterUpdate {
    constructor(private tag: String, private value: number){}
}
import {Observable, Subscription} from "rxjs";
import { HttpClient, HttpParams } from '@angular/common/http';

import {ReportData} from './reportData';

export class ReportDataGetter {

    private reportData: ReportData[] = [];
    
    private tags: String[] = ["LEVEL_1", "LEVEL_2", "LEVEL_3", "VALVE_1", "VALVE_2", "VALVE_3", "VALVE_4", "OUTPUT"]
    private urlAndonDataReport: string = "http://localhost:8010/andonReport";
    private urlChangeParameterDataReport: string = "http://localhost:8010/changeParamterReport";
    private urlHistoryDataReport: string = "http://localhost:8010/historyReport";
    private urlWorkDataReport: string = "http://localhost:8010/workReport";
    private andonReportGetterClass: NotificationData = new AndonData(this.http, this.urlAndonDataReport);
    private workReportGetterClass: NotificationData = new NotificationData(this.http, this.urlWorkDataReport);
    private historyReportGetterClass: NotificationData = new NotificationData(this.http, this.urlHistoryDataReport);
    private changeParameterReportGetterClass: NotificationData = new NotificationData(this.http, this.urlChangeParameterDataReport);
    
    constructor(private http: HttpClient) {}

    
    //--------------------------------------------------------
    //                  Andon Report data
    //--------------------------------------------------------
    public getAndonReportData() {
        return this.andonReportGetterClass.getData();
    }

    public andonReportGetter(): Subscription {
        return this.andonReportGetterClass.intervalDataGetter();
    }

    public initAndonReportDataGetter() {
        return this.andonReportGetterClass.initDataGetter()
    }

    private getCurrentAndonReportData(startId: Number = 0) {     
        this.andonReportGetterClass.getCurrentData()
    }

    //--------------------------------------------------------
    //                  History Report data
    //--------------------------------------------------------
    public getHistoryReportData() {
        return this.historyReportGetterClass.getData();
    }

    public historyReportGetter(): Subscription {
        return this.historyReportGetterClass.intervalDataGetter();
    }

    public initHistoryReportDataGetter() {
        return this.historyReportGetterClass.initDataGetter()
    }

    private getCurrentHistoryReportData(startId: Number = 0) {     
        this.historyReportGetterClass.getCurrentData()
    }

    //--------------------------------------------------------
    //                  Work Report data
    //--------------------------------------------------------
    public getWorkReportData() {
        return this.workReportGetterClass.getData();
    }

    public workReportGetter(): Subscription {
        return this.workReportGetterClass.intervalDataGetter();
    }

    public initWorkReportDataGetter() {
        return this.workReportGetterClass.initDataGetter()
    }

    private getCurrentWorkReportData(startId: Number = 0) {     
        this.workReportGetterClass.getCurrentData()
    }

    //--------------------------------------------------------
    //                  Change parameter Report data
    //--------------------------------------------------------
    public getChangeParameterReportData() {
        return this.changeParameterReportGetterClass.getData();
    }

    public changeParameterReportGetter(): Subscription {
        return this.changeParameterReportGetterClass.intervalDataGetter();
    }

    public initChangeParameterReportDataGetter() {
        return this.changeParameterReportGetterClass.initDataGetter()
    }

    private getCurrentChangeParameterReportData(startId: Number = 0) {     
        this.changeParameterReportGetterClass.getCurrentData()
    }
}

class NotificationData {

    constructor(protected http: HttpClient, protected url: String, protected data: ReportData[] = []) {}

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
        
        let andons: ReportData[] = [];
        let params = new HttpParams().set('startIndex', startId.toString());
        return this.http.get<ReportData []>(this.url.toString(), {params})
        .forEach(r => {
        r.forEach(e => {
            let rec: ReportData = new ReportData(e["stateSpaceTag"], e["date"], e["value"], e["id"])
            andons.push(rec);
        })
        this.data = andons;
        })
    }
}

class AndonData extends NotificationData {
    constructor(protected http: HttpClient, protected url: String, protected data: ReportData[] = []) {
        super(http, url, data)
    }

    public getCurrentData(startId: Number = 0) {
        
        let andons: ReportData[] = [];
        let params = new HttpParams().set('startIndex', startId.toString());
        return this.http.get<ReportData []>(this.url.toString(), {params})
        .forEach(r => {
        r.forEach(e => {
            let rec: ReportData = new ReportData(e["stateSpaceTag"], e["date"], e["value"], e["id"], e["type"], e["limitTag"])
            andons.push(rec);
        })
        this.data = andons;
        })
    }
}


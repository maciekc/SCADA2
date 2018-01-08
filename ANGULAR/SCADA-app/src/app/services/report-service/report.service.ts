import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";
import { ReportDataGetter } from '../../class/reportDataGetter';

@Injectable()
export class ReportService {

  private subscriptions: Subscription [] = [];
  private reportDataGetter: ReportDataGetter;

  constructor(private http: HttpClient) {
    this.reportDataGetter = new ReportDataGetter(this.http);
  }


  public getAndonReportData() {
    return this.reportDataGetter.initAndonReportDataGetter()
  }

  public getHistoryReportData() {
    return this.reportDataGetter.initHistoryReportDataGetter()
  }

  public getChangeParameterReportData() {
    return this.reportDataGetter.initChangeParameterReportDataGetter()
  }

  public getWorkReportData() {
    return this.reportDataGetter.initWorkReportDataGetter()
  }

}
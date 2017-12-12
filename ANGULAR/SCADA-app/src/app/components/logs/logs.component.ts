import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";

import {AndonService} from '../../services/andonService/andon.service';
import {ReportService} from '../../services/report-service/report.service';
import {CommonService} from '../../services/common-service/common.service';
import {ReportDataGetter} from '../../class/reportDataGetter';
import {Andon} from '../../class/andon';
import {ReportData} from '../../class/reportData';
import { from } from 'rxjs/observable/from';


@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})


export class LogsComponent implements OnInit {

  private serviceSubscriptions: Subscription [] = [];
  private limitsData: Map<String, String> = new Map();
  private stateVariableData: Map<String, String> = new Map();
  private reportData: Promise<ReportData[]>;
  // private reportData: ReportData[];
  private reportType: String = "ANDON";
  
  
  @Input() rowData: ReportData;

  constructor(private reportService: ReportService, private commonService: CommonService) { 
    this.limitsData = commonService.getLimitsData();
    this.stateVariableData = commonService.getStateVariableData();
    console.log(this.stateVariableData)
    // this.reportData.push(new ReportData("LEVEL_1","201",12,1,1,"LEVEL_1_MIN"));
    // this.reportData.push(new ReportData("LEVEL_1","dsadas",343,2,1,"LEVEL_1_MIN"));
    // this.reportData.push(new ReportData("LEVEL_1","dsadas",343,2,1,"LEVEL_1_MIN"))
  }


  generateReport(event) {
    console.log("generate Report")
    console.log(this.reportType)
    switch (this.reportType) {
      case "WORK": {
        this.reportData = this.reportService.getWorkReportData()
        // this.reportService.getWorkReportData()
        // .then(r => this.reportData = r)
        break
      }
      case "ANDON": {
        this.reportData = this.reportService.getAndonReportData()
        // this.reportService.getAndonReportData()
        // .then(r => {this.reportData = r;    console.log("tu")})               
        break
      }
      case "CHANGE_VALUE": {
       
        // this.reportData = this.reportService.getChangeParameterReportData()
        this.reportData = this.reportService.getChangeParameterReportData()
        this.reportService.getChangeParameterReportData().then(r => 
        console.log(r))
        break
        // .then(r => this.reportData = r)
      } 
    }
  }

  changeReportType(event) {
    console.log(event)
    let target = event.target;
    this.reportType = target.id;
    document.getElementById("ReportTypeButton").innerText = target.innerText;
  }

  ngOnInit() {

  }

  ngOnDestroy() {
    console.log("on destroy")
  }

  public getLimitName(r: ReportData) {
    // console.log(this.limitsData.get(andon.getLimitTag()))
    return this.limitsData.get(r.getReasonTag())
  }

  public getStateVariableName(r: ReportData) {
    // console.log(r)
    return this.stateVariableData.get(r.getStateVariableTag())
  }

  private stopService() {
    this.serviceSubscriptions.forEach(s => s.unsubscribe());
    this.serviceSubscriptions = [];
  }
  changeMessageStyle() {
    return "reportRow"
  }

}

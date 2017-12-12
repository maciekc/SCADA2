import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, Subscription} from "rxjs";

import {AndonService} from '../../services/andonService/andon.service';
import {CommonService} from '../../services/common-service/common.service'
import {Andon} from '../../class/andon';
import { from } from 'rxjs/observable/from';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  private serviceSubscriptions: Subscription [] = [];
  private andonData: Andon[] = [];
  private limitsData: Map<String, String> = new Map();
  private stateVariableData: Map<String, String> = new Map();
  

  private style = "infoDialog";
  private iconStyle = "fa fa-info-circle";
  @Input() message: Andon;

  constructor(private andonService: AndonService, private commonService: CommonService) { 
    this.limitsData = commonService.getLimitsData();
    this.stateVariableData = commonService.getStateVariableData();
  }

  ngOnInit() {

    this.andonService.startService()

    this.andonService.initAndonData()
    .then(r => {
      if(r.length > 0) {
        this.andonData = r
      }
    });


    let andon = Observable.interval(1000)
    .subscribe(r => {
      let data = this.andonService.getAndonData();
      if(data.length > 0 && data.length != this.andonData.length) {
        this.andonData = this.andonData.concat(data)
      }
    })
    this.serviceSubscriptions.push(andon);
  }

  ngOnDestroy() {
    console.log("on destroy")
    this.andonService.stopService();
    this.stopService()
  }

  public changeMessageStyle(andon: Andon) {
    if (andon.getType() == -2 || andon.getType() == 2) {
      this.style = "errorDialog"
    } else {
      if (andon.getType() == -1 || andon.getType() == 1) {
        this.style = "warningDialog"
      } else {
        this.style = "infoDialog"
      }
    } 
    return this.style;
  }

  public setIconClass(andon: Andon) {
    if (this.style == "errorDialog") {
      this.iconStyle = "fa fa-exclamation-triangle"
    } else {
      this.iconStyle = "fa fa-info-circle"
    }
    return this.iconStyle;
  }

  public getLimitName(andon: Andon) {
    return this.limitsData.get(andon.getLimitTag())
  }

  public getStateVariableName(andon: Andon) {
    return this.stateVariableData.get(andon.getStateVariableTag())
  }

  private stopService() {
    this.serviceSubscriptions.forEach(s => s.unsubscribe());
    this.serviceSubscriptions = [];
  }

}

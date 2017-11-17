import { Component, OnInit, Input } from '@angular/core';

import { Message } from '../../class/message';
import {Andon} from '../../class/andon';

@Component({
  selector: 'app-info-messages',
  templateUrl: './info-messages.component.html',
  styleUrls:  ['../messages/messages.component.css']
})
export class InfoMessagesComponent implements OnInit {

  
  // private messageTypes: Map<number, String> = [-1: "", -2: "", 1: "", 2: "", 0: ""];
  private style = "infoDialog";
  private iconStyle = "fa fa-info-circle";
  @Input() message: Andon;
  constructor() { }

  ngOnInit() {
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

}

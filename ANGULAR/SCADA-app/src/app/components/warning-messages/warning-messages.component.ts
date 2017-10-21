import { Component, OnInit, Input } from '@angular/core';

import { Message } from '../../class/message'

@Component({
  selector: 'app-warning-messages',
  templateUrl: './warning-messages.component.html',
  styleUrls:  ['../messages/messages.component.css']
})
export class WarningMessagesComponent implements OnInit {

  @Input() message : Message
  constructor() { }

  ngOnInit() {
  }

}

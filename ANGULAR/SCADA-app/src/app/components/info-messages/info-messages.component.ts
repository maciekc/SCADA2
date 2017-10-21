import { Component, OnInit, Input } from '@angular/core';

import { Message } from '../../class/message'

@Component({
  selector: 'app-info-messages',
  templateUrl: './info-messages.component.html',
  styleUrls:  ['../messages/messages.component.css']
})
export class InfoMessagesComponent implements OnInit {

  @Input() message: Message;
  constructor() { }

  ngOnInit() {
  }

}

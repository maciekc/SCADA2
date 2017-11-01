import { Component, OnInit } from '@angular/core';

import {Message} from '../../class/message'


@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  message: Message;
  message1 : Message;
  constructor() { }

  ngOnInit() {
    this.message = new Message("sterowanie", "2017-10-19 12:00:00", "Limit górny");
    this.message1 = new Message("wyjście", "2017-10-19 21:00:00", "Limit dolny");
  }

}

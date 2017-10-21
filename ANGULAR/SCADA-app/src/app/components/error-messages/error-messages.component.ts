import { Component, OnInit, Input } from '@angular/core';

import { Message } from '../../class/message'


@Component({
  selector: 'app-error-messages',
  templateUrl: './error-messages.component.html',
  styleUrls: ['../messages/messages.component.css']
})
export class ErrorMessagesComponent implements OnInit {

  @Input() message: Message;
  constructor() { }

  ngOnInit() {
  }

}

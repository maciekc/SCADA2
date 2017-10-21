import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';
import { LogsComponent } from './components/logs/logs.component'
import { PlantComponent } from './components/plant/plant.component'
import { ControlerComponent } from './components/controler/controler.component'
import { StatisticsComponent } from './components/statistics/statistics.component';
import { MessagesComponent } from './components/messages/messages.component';
import { WarningMessagesComponent } from './components/warning-messages/warning-messages.component';
import { ErrorMessagesComponent } from './components/error-messages/error-messages.component';
import { InfoMessagesComponent } from './components/info-messages/info-messages.component'

@NgModule({
  declarations: [
    AppComponent,
    LogsComponent,
    PlantComponent,
    ControlerComponent,
    StatisticsComponent,
    MessagesComponent,
    WarningMessagesComponent,
    ErrorMessagesComponent,
    InfoMessagesComponent
  ],
  imports: [BrowserModule,
    FormsModule,
    HttpClientModule,
    
    CommonModule,
    RouterModule.forRoot([
      {
        path: 'statistics',
        component : StatisticsComponent
      },
      {
        path: 'logs',
        component : LogsComponent
      },
      {
        path: 'plant',
        component : PlantComponent
      },
      {
        path: 'controler',
        component : ControlerComponent
      }

    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

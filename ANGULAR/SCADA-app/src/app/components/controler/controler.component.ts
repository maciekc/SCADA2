import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable} from "rxjs";
import 'rxjs';

declare var Plotly: any;

@Component({
  selector: 'app-controler',
  templateUrl: './controler.component.html',
  styleUrls: ['./controler.component.css']
})
export class ControlerComponent implements OnInit {

  constructor() { }
  
    ngOnInit() {
      let figure = document.getElementById('outputPlot');
      // console.log("fig " + figure.tagName)
      let outpuData = {
        type: 'scatter',
        x: [1, 2, 3, 4, 5],
        y: [1, 2, 4, 8, 16],
        line: {color: 'blue'},
        mode: 'lines',
        name: 'wyjście'
      };
  
      let reference = {
        type: 'scatter',
        x: [1, 2, 3, 4, 5],
        y: [0, 3, 4, 4, 4],
        line: {color: 'red'},
        mode: 'lines',
        name: 'wart. zadana'
      };
      Plotly.newPlot(figure, [outpuData, reference], {
        margin: { t: 0 }, 
        xaxis : {title : 'Czas [s]'},
        yaxis : {title : 'Temeratura [^C]'} } );
  
      let figure2 = document.getElementById('controlPlot');
      // console.log("fig " + figure.tagName)
      let data = [{
        x: [1, 2, 3, 4, 5],
        y: [1, 2, 4, 8, 16],
      }];
      let layout = {
        margin: { t: 0 },
        xaxis : {title : 'Czas [s]'},
        yaxis : {title : 'Napięcie [V]'}
      }
      Plotly.newPlot(figure2, data, layout);
    }
  }
  

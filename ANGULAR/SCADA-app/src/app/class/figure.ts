import { HttpClient, HttpParams } from '@angular/common/http';
declare var Plotly: any;

export abstract class Figure {
    
    protected layout;
    
    constructor(protected figureId: HTMLElement, protected xLabel: String, protected yLabel: String) {
        this.layout = {
            margin: { t: 0 }, 
            xaxis : {title : this.xLabel},
            yaxis : {title : this.yLabel}
        }
        this.createPlotData([], [], "")
    }


    protected plotDataSeries(dates: String [], values: number [], name: String, lineColor = 'rgb(2, 119, 216)', type ='scatter', mode = 'lines'):any {
        return {
            type: type,
            x: dates,
            y: values,
            line: {color: lineColor},
            mode: mode,
            name: name
            };
    }

    public updatePlotData(dates: String [], values: number [], name: String, lineColor = 'rgb(2, 119, 216)', type ='scatter', mode = 'lines') {
        let newData = this.plotDataSeries(dates, values, name, lineColor, type, mode)
        Plotly.newPlot(this.figureId, [newData], this.layout)
    }

    public createPlotData(dates: String [], values: number [], name: String, lineColor = 'rgb(2, 119, 216)', type ='scatter', mode = 'lines') {
        let newData = this.plotDataSeries(dates, values, name, lineColor, type, mode)
        Plotly.newPlot(this.figureId, [newData], this.layout)
    }

}

export class LineFigure extends Figure {

    protected layout;

    constructor(figureId: HTMLElement, xLabel: String, yLabel: String) {
        super(figureId, xLabel, yLabel);     
    }


    protected plotDataSeries(dates: String [], values: number [], name: String, lineColor = 'rgb(2, 119, 216)', type ='scatter', mode = 'lines') {
        return {
            type: type,
            x: dates,
            y: values,
            line: {color: lineColor},
            mode: mode,
            name: name
          };
    }

}

export class AreaFigure extends Figure {

    constructor(figureId: HTMLElement, xLabel: String, yLabel: String) {
        super(figureId, xLabel, yLabel);     
    }
   
    protected plotDataSeries(dates: String [], values: number [], name: String, fill = 'tozeroy', type ='scatter', mode = 'lines'): any {
        return {
            type: type,
            x: dates,
            y: values,
            fill: fill,
            name: name
          };
    }

}

export class BarFigure extends Figure {
    
        constructor(figureId: HTMLElement, xLabel: String, yLabel: String) {
            super(figureId, xLabel, yLabel);     
        }
       
        protected plotDataSeries(dates: String [], values: number [], name: String, color = 'rgb(0, 220, 131)', type ='bar', mode = 'lines'): any {
            console.log("bar")
            return {
                x: dates,
                y: values,
                type: 'bar',
                marker: {
                    color: 'rgb(0, 220, 131)'
                }
              };
        }
    
    }
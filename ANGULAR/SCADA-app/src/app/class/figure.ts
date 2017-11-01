import { HttpClient, HttpParams } from '@angular/common/http';
declare var Plotly: any;

export class Figure {

    private layout;

    constructor(private figureId: HTMLElement, private xLabel: String,private yLabel: String) {
        this.layout = {
            margin: { t: 0 }, 
            xaxis : {title : this.xLabel},
            yaxis : {title : this.yLabel}
        }
        this.createPlotData([], [], "")
    }


    private plotDataSeries(dates: String [], values: number [], name: String, lineColor = 'blue', type ='scatter', mode = 'lines') {
        return {
            type: type,
            x: dates,
            y: values,
            line: {color: lineColor},
            mode: mode,
            name: name,
          };
    }

    public updatePlotData(dates: String [], values: number [], name: String, lineColor = 'blue', type ='scatter', mode = 'lines') {
        let newData = this.plotDataSeries(dates, values, name, lineColor, type, mode)
        console.log(newData)
        Plotly.newPlot(this.figureId, [newData], this.layout)
    }

    public createPlotData(dates: String [], values: number [], name: String, lineColor = 'blue', type ='scatter', mode = 'lines') {
        let newData = this.plotDataSeries(dates, values, name, lineColor, type, mode)
        Plotly.newPlot(this.figureId, [newData], this.layout)
    }

}
import { HttpClient, HttpParams } from '@angular/common/http';


export class Label {

    constructor(private label: HTMLElement, private unit: String) {}

    // public setLabelValue(value: number) {
    //     this.label.innerText = value.toString() + " " + this.unit;
    // }
    public setLabelValue(value: String) {
        this.label.innerText = value + " " + this.unit;
    }
}
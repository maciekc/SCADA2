export class Point {
    private date: String;
    private value: number;

    constructor(date: String, value: number) {
        this.date = date;
        this.value = value;
    }

    public getDate() {
        return this.date;
    }
    public getValue() {
        return this.value;
    }
}
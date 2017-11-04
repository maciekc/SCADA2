export class StateVariableData {
    
    constructor(private tag: String, private date: String,private value: number) {}

    public getDate() {
        return this.date;
    }
    public getValue() {
        return this.value;
    }
    public getTag() {
        return this.tag;
    }
}
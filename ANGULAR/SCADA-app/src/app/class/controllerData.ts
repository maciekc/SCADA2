export class ControllerData {
    
    constructor(private tag: String, private name: String, private value: number) {}

    public getValue() {
        return this.value;
    }
    public getTag() {
        return this.tag;
    }
    public getName() {
        return this.name;
    }
}
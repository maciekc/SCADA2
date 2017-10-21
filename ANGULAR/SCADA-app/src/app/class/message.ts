export class Message {
    private reason;
    private date;
    private type;

    constructor(reason, date, type) {
        this.reason = reason;
        this.date = date;
        this.type = type;
    }

    public getDate() {
        return this.date;
    }
    public getReason() {
        return this.reason;
    }
    public getType() {
        return this.type;
    }
}
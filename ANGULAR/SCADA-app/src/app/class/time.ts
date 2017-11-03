export class Time {
    
    date: Date;

    constructor() {
        this.date = new Date();
        setInterval(() => {
            this.date = new Date();
        }, 1000);
    }

    

}
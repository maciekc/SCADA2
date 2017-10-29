package com.scada.server;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import com.scada.dataBase.GetDBData;

public class NotificationActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(GetDBData getDBData) {
        return Props.create(NotificationActor.class, () -> new NotificationActor(getDBData));
    }

    private final GetDBData getDBData;

    public NotificationActor(GetDBData getDBData) {
        this.getDBData = getDBData;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(NotificationData.class, m -> {
                    if(m.getStartDate() == "") {
                        getDBData.getNotifications()
                                .subscribe(v -> {
                                    log.info("Notification data: {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    } else {
                        getDBData.getNotifications(m.getStartDate(), m.getEndDate())
                                .subscribe(v -> {
                                    log.info("Notification data: {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    }
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    static public class NotificationData {
        private final String startDate;
        private final String endDate;

        public NotificationData(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public NotificationData() {
            this.startDate = "";
            this.endDate = "";
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }
    }
}

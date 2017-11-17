package com.scada.server;

import static akka.pattern.PatternsCS.ask;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import com.scada.dataBase.GetDBData;

import com.scada.server.OPCDataLoggerActor.*;
import java.util.concurrent.CompletionStage;

public class NotificationActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(GetDBData getDBData) {
        return Props.create(NotificationActor.class, () -> new NotificationActor(getDBData));
    }

    private final GetDBData getDBData;
    private ActorRef serverRef;

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
                                    getSender().tell(v, getSelf());
                                });
                    } else {
                        getDBData.getNotifications(m.getStartDate(), m.getEndDate())
                                .subscribe(v -> {
                                    log.info("Notification data: {}", v);
                                    getSender().tell(v, getSelf());
                                });
                    }
                })
                .match(AndonRequest.class, m -> {
                    this.serverRef = getSender();
                    m.getOPCDataLoggerActor().tell(new OPCDataLoggerActor.AndonRequest(), getSelf());
                })
                .match(UpdateAndonData.class, m -> {
                    if (m.isUpdate() == true) {
                        log.info("new andon to log in DB.");
                        getDBData.getNotifications()
                                .subscribe(v -> {
//                                    this.serverRef.tell(new AndonRequestData(new Gson().toJson(v)), getSelf());
                                    this.serverRef.tell(v, getSelf());
                                });
                    } else {
                        log.info("any andon to log");
                        this.serverRef.tell("kk", getSelf());
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
    static public class AndonRequest {
        private final ActorRef OPCDataLoggerActor;

        public AndonRequest(ActorRef OPCDataLoggerActor) {
            this.OPCDataLoggerActor = OPCDataLoggerActor;
        }

        public ActorRef getOPCDataLoggerActor() {
            return OPCDataLoggerActor;
        }
    }
    static public class AndonRequestData {
        private final String data;

        public AndonRequestData(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }
}

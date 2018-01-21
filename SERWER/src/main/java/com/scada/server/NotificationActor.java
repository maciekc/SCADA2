package com.scada.server;

import static akka.pattern.PatternsCS.ask;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import com.scada.dataBase.GetDBData;

import com.scada.model.dataBase.Andon.Andon;
import com.scada.server.OPCDataLoggerActor.*;
import rx.Completable;
import rx.schedulers.Schedulers;
import rx.Observable;


import java.util.concurrent.CompletionStage;

public class NotificationActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(GetDBData getDBData) {
        return Props.create(NotificationActor.class, () -> new NotificationActor(getDBData));
    }

    private final GetDBData getDBData;
    private ActorRef serverRef;
    private final Mail mail;
    private int id = -1;


    public NotificationActor(GetDBData getDBData) {
        this.mail = new Mail();
        this.getDBData = getDBData;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                //----------------------------------------------
                //              NOTIFICATION REQUESTS
                //----------------------------------------------
                .match(NotificationRequest.class, m ->
                    getDBData.getNotifications()
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(v -> {
                                log.info("Notification data: {}", v);
                                getSender().tell(v, getSelf());
                            })
                )
                .match(NotificationRequestDateRange.class, m ->
                    getDBData.getNotifications(m.getStartDate(), m.getEndDate())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(v -> {
                                log.info("Notification data: {}", v);
                                getSender().tell(v, getSelf());
                            })
                )

                //----------------------------------------------
                //              ANDON REQUESTS
                //----------------------------------------------
                .match(AndonRequest.class, m -> {
                    this.serverRef = getSender();
                    getDBData.getAndonData(m.getStartId())
                            .subscribe(v -> {
                                System.out.println("values " + v);
                                getSender().tell(v, getSelf());
                                Observable.from(v)
                                        .observeOn(Schedulers.newThread())
                                        .forEach(andon -> {
                                            if(andon.getId() > id) {
                                                id = andon.getId();
                                                getDBData.setMaxId(id);
                                            }
//                                            mail.sendMail(andon);
                                        });
                            });
                })
                .match(UpdateAndonData.class, m -> {
                    if (m.isUpdate() == true) {
                        log.info("new andon to log in DB.");
                        getDBData.getNotifications()
                                .subscribe(v -> {
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

    static public class NotificationRequestDateRange {
        private final String startDate;
        private final String endDate;

        public NotificationRequestDateRange(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }
    }

    static public class NotificationRequest {

        public NotificationRequest() {}

    }

    static public class AndonRequest {
    private final int StartId;

    public AndonRequest(int StartId) {
        this.StartId = StartId;
    }

    public int getStartId() {
        return StartId;
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

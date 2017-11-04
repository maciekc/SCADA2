package com.scada.server;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.scada.dataBase.GetDBData;
import com.scada.dataBase.InsertDataToDB;
import com.scada.model.dataBase.Andon.Andon;
import com.scada.model.dataBase.Controller.Controller;
import com.scada.model.dataBase.Work.Work;
import rx.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.scada.server.OPCServer.*;


public class OPCDataLoggerActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(InsertDataToDB insertDataToDB, OPCServer opcServer) {
        return Props.create(OPCDataLoggerActor.class, () -> new OPCDataLoggerActor(insertDataToDB, opcServer));
    }

    private final InsertDataToDB insertDataToDB;
    private final OPCServer opcServer;
    private ActorRef notificationRef;

    public OPCDataLoggerActor(InsertDataToDB insertDataToDB, OPCServer opcServer) {
        this.insertDataToDB = insertDataToDB;
        this.opcServer = opcServer;
    }

    private void logAndonData() {

                    final List<Andon> andonData = opcServer.getAndonData();
                    if(andonData.size() > 0 ) {
                        Observable.merge(andonData.stream()
                                .map(r -> insertDataToDB.logAndonRecord(r))
                                .collect(Collectors.toList()))
                                .subscribe(res -> log.info("Log andon in DB."));

                        this.notificationRef.tell(new UpdateAndonData(true), getSelf());
                    } else {this.notificationRef.tell(new UpdateAndonData(false), getSelf());}
    }

    private void logAndonData0() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(t -> {
                    final List<Andon> andonData = opcServer.getAndonData();
                    if(andonData.size() > 0 ) {
                        Observable.merge(andonData.stream()
                        .map(r -> insertDataToDB.logAndonRecord(r))
                        .collect(Collectors.toList()))
                        .subscribe(res -> log.info("Log andon in DB."));

                        this.notificationRef.tell(new UpdateAndonData(true), getSelf());
                    } else {this.notificationRef.tell(new UpdateAndonData(false), getSelf());}}
                );
    }

    private void logOPCData() {
        Observable.interval(5, TimeUnit.SECONDS)
                .subscribe(t -> {
                            Observable.merge(opcServer.getControllersData().stream()
                                    .map(r -> insertDataToDB.logControllerRecord(new Controller(1, r.getTag(), r.getMode(), r.getValue(), r.getDate())))
                                    .collect(Collectors.toList())
                            ).subscribe(res -> log.info("Log controller data in DB."));
                            Observable.merge(opcServer.getSystemVariableData().stream()
                                    .map(r -> insertDataToDB.logWorkRecord(new Work(1, r.getTag(), r.getValue(), r.getDate())))
                                    .collect(Collectors.toList())
                            ).subscribe(res -> log.info("Log data in DB."));
                        }
                );
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartLogging.class, m -> {
                    log.info("Start logging data from OPC Server.");
                    this.logOPCData();
                })
                .match(EndLogging.class, m -> {
                    log.info("End logging data from OPC Server.");
                })
                .match(AndonRequest.class, m -> {
                    this.notificationRef = getSender();
                    this.logAndonData();
                })
                .matchAny(o -> log.info("receive unknown message"))
                .build();
    }

    //------------------------------------------------
    //              MESSAGES CLASSES
    //------------------------------------------------

    public static class StartLogging {
        private final String state = "START_LOGGING";
    }
    public static class EndLogging {
        private final String state = "END_LOGGING";
    }

    public static class AndonRequest {}
    public static class UpdateAndonData {
        private final boolean update;

        public UpdateAndonData(boolean update) {
            this.update = update;
        }

        public boolean isUpdate() {
            return update;
        }
    }

}

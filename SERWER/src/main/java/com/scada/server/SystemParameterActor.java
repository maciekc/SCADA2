package com.scada.server;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.scada.dataBase.InsertDataToDB;
import com.scada.dataBase.UpdateDataInDB;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class SystemParameterActor extends AbstractActor {


    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(UpdateDataInDB updateDataInDB, InsertDataToDB insertDataToDB) {
        return Props.create(SystemParameterActor.class, () -> new SystemParameterActor(updateDataInDB, insertDataToDB));
    }

    private final UpdateDataInDB updateDataInDB;
    private final InsertDataToDB insertDataToDB;

    public SystemParameterActor(UpdateDataInDB updateDataInDB, InsertDataToDB insertDataToDB) {
        this.updateDataInDB = updateDataInDB;
        this.insertDataToDB = insertDataToDB;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(LimitUpdate.class, m -> {
//                        Observable.concat(updateDataInDB.updateLimit(m.getTag(), m.getValue(), m.getType()),
//                                insertDataToDB.insertChangeParameterValue(
//                                        new ChangeParameterValue(1, m.getTag(), m.getValue(), )))
                                System.out.println("in spar " + m.getTag() + " " + m.getValue() + " " + m.getType());
                        updateDataInDB.updateLimit(m.getTag(), m.getValue(), m.getType())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(v -> {
                                log.info("LIMIT UPDATE" );
                                getSender().tell("OK", getSelf());
                            });}
                )
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    //-----------------------------------------------------------
    //                      MESSAGES CLASS
    //-----------------------------------------------------------


    public static class LimitUpdate {
        private String tag;
        private double value;
        private int type;

        public LimitUpdate(String tag, double value, int type) {
            this.tag = tag;
            this.value = value;
            this.type = type;
        }
        public LimitUpdate(String tag) {
            this.tag = tag;
            this.value = 0.;
        }

        public LimitUpdate() {
            this.tag = "";
            this.value = 0.;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}

package com.scada.server;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.scada.dataBase.InsertDataToDB;
import com.scada.dataBase.UpdateDataInDB;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.Controller.ControllerParameter;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;

public class SystemParameterActor extends AbstractActor {


    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(UpdateDataInDB updateDataInDB, InsertDataToDB insertDataToDB) {
        return Props.create(SystemParameterActor.class, () -> new SystemParameterActor(updateDataInDB, insertDataToDB));
    }

    private final UpdateDataInDB updateDataInDB;
    private final InsertDataToDB insertDataToDB;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Calendar calendar = Calendar.getInstance();

    public SystemParameterActor(UpdateDataInDB updateDataInDB, InsertDataToDB insertDataToDB) {
        this.updateDataInDB = updateDataInDB;
        this.insertDataToDB = insertDataToDB;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                .match(LimitUpdate.class, (LimitUpdate m) ->
                        updateLimit(m)
//                        updateDataInDB.updateLimit(m.getTag(), m.getValue(), m.getType())
//                            .map(r -> {
//                            getSender().tell("OK", getSelf());
//                            System.out.println("tu " + m.getType());
//                            final Date date = calendar.getTime();
//                            String date_str = dateFormat.format(date);
//                            return insertDataToDB.insertChangeParameterValue(new ChangeParameterValue(1, m.getTag(), m.getValue(), date_str));
//                        })
//                                .subscribeOn(Schedulers.fromCallableom(Executors.newFixedThreadPool(4)))
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(v -> {
                                log.info("LIMIT UPDATE" );
                                getSender().tell("OK", getSelf());
                            })
                )
                .match(ControllerParameter.class, (ControllerParameter m) ->
                        updateControllerParameter(m)
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(v -> {
                                log.info("LIMIT UPDATE" );
                                getSender().tell("OK", getSelf());
                            })
                )
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private Observable<Integer> updateLimit(LimitUpdate m) {
        final Date date = calendar.getTime();
        String date_str = dateFormat.format(date);

        return Observable.concat(updateDataInDB.updateLimit(m.getTag(), m.getValue(), m.getType()),
                insertDataToDB.insertChangeParameterValue(new ChangeParameterValue(1, m.getTag(), m.getValue(), date_str)));
    }

    private Observable<Integer> updateControllerParameter(ControllerParameter m) {
        final Date date = calendar.getTime();
        String date_str = dateFormat.format(date);
        return Observable.concat(updateDataInDB.updateControllerParameter(m.getTag(), m.getValue()),
                insertDataToDB.insertChangeParameterValue(new ChangeParameterValue(1,m.getTag(), m.getValue(), date_str)));
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

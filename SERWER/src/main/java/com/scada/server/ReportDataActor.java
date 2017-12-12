package com.scada.server;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import com.scada.dataBase.GetDBData;
import rx.schedulers.Schedulers;

public class ReportDataActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(GetDBData getDBData) {
        return Props.create(ReportDataActor.class, () -> new ReportDataActor(getDBData));
    }

    private final GetDBData getDBData;
    private ActorRef serverActor;

    public ReportDataActor(GetDBData getDBData) {
        this.getDBData = getDBData;
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AndonData.class, m -> {
                            ActorRef serverActor = getSender();
                            getDBData.getAndonData(-1)
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(v -> {
                                        log.info("Andon data: {}", v);
                                        serverActor.tell(v, getSelf());
                                    });
                        }
                )
                .match(AndonDataDateRange.class, m -> {
                            ActorRef serverActor = getSender();
                            getDBData.getAndonData(m.getStartDate(), m.getEndDate())
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(v -> {
                                        log.info("Andon data: {}", v);
                                        serverActor.tell(v, getSelf());
                                    });
                        }
                )
                .match(WorkData.class, m -> {
                            ActorRef serverActor = getSender();
                            getDBData.getWorkData()
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(v -> {
                                        log.info("Work data : {}", v);
                                        serverActor.tell(v, getSelf());
                                    });
                        }
                )
                .match(WorkDataDateRange.class, m-> {
                            ActorRef serverActor = getSender();
                            getDBData.getWorkData(m.getStartDate(), m.getEndDate())
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(v -> {
                                        log.info("Work data : {}", v);
                                        serverActor.tell(v, getSelf());
                                    });
                        }
                )
                .match(ControllerData.class, m -> {
                            ActorRef serverActor = getSender();
                            getDBData.getWorkData()
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(v -> {
                                        log.info("Controller data : {}", v);
                                        serverActor.tell(v, getSelf());
                                    });
                        }
                )
                .match(ControllerDataDateRange.class, m -> {
                            ActorRef serverActor = getSender();
                            getDBData.getWorkData(m.getStartDate(), m.getEndDate())
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(v -> {
                                        log.info("Controller data : {}", v);
                                        serverActor.tell(v, getSelf());
                                    });
                        }
                )
                .match(ChangeParameterValueData.class, m -> {
                            ActorRef serverActor = getSender();
                            getDBData.getChangeParameterValueData()
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(v -> {
                                        log.info("CPV data : {}", v);
                                        serverActor.tell(v, getSelf());
                                    });
                        }
                )
                .match(ChangeParameterValueDataDateRange.class, m -> {
                            ActorRef serverActor = getSender();
                            getDBData.getChangeParameterValueData(m.getStartDate(), m.getEndDate())
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(v -> {
                                        log.info("CPV data : {}", v);
                                        serverActor.tell(v, getSelf());
                                    });
                        }
                )
                .matchAny(o -> log.info("received unknown mesasage"))
                .build();
    }

    //------------------------------------------------
    //              MESSAGES CLASS
    //------------------------------------------------

    static private abstract class DateRange {
        protected final String startDate;
        protected final String endDate;

        public DateRange(String startDate, String endDate) {
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

    static public class AndonData {

        private final String stateVariableTag;

        public AndonData() {
            this.stateVariableTag = "";
        }

        public AndonData(String stateVariableTag) {
            this.stateVariableTag = stateVariableTag;
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }
    static public class AndonDataDateRange extends DateRange {

        private final String stateVariableTag;

        public AndonDataDateRange(String startDate, String endDate) {
            super(startDate, endDate);
            this.stateVariableTag = "";
        }

        public AndonDataDateRange(String startDate, String endDate, String stateVariableTag) {
            super(startDate, endDate);
            this.stateVariableTag = stateVariableTag;
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }

    static public class WorkData {
        private final String stateVariableTag;

        public WorkData() {
            this.stateVariableTag = "";
        }

        public WorkData(String stateVariableTag) {
            this.stateVariableTag = stateVariableTag;
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }
    static public class WorkDataDateRange extends DateRange {

        private String stateVariableTag;

        public WorkDataDateRange(String startDate, String endDate) {
            super(startDate, endDate);
            this.stateVariableTag = "";
        }

        public WorkDataDateRange(String startDate, String endDate, String stateVariableTag) {
            super(startDate, endDate);
            this.stateVariableTag = stateVariableTag;
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }

    static public class ControllerData {

        private String stateVariableTag;

        public ControllerData(String stateVariableTag) {
            this.stateVariableTag = stateVariableTag;
        }

        public ControllerData() {
            this.stateVariableTag = "";
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }
    static public class ControllerDataDateRange extends DateRange {

        private String stateVariableTag;

        public ControllerDataDateRange(String startDate, String endDate, String stateVariableTag) {
            super(startDate, endDate);
            this.stateVariableTag = stateVariableTag;
        }

        public ControllerDataDateRange(String startDate, String endDate) {
            super(startDate, endDate);
            this.stateVariableTag = "";
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }

    static public class ChangeParameterValueData {

        private String stateVariableTag;

        public ChangeParameterValueData(String stateVariableTag) {
            this.stateVariableTag = stateVariableTag;
        }

        public ChangeParameterValueData() {
            this.stateVariableTag = "";
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }
    static public class ChangeParameterValueDataDateRange extends DateRange {

        private String stateVariableTag;

        public ChangeParameterValueDataDateRange(String startDate, String endDate, String stateVariableTag) {
            super(startDate, endDate);
            this.stateVariableTag = stateVariableTag;
        }

        public ChangeParameterValueDataDateRange(String startDate, String endDate) {
            super(startDate, endDate);
            this.stateVariableTag = "";
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }

}

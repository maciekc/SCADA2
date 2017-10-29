package com.scada.server;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import com.scada.dataBase.GetDBData;

public class ReportDataActor extends AbstractActor {

        private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(GetDBData getDBData) {
        return Props.create(ReportDataActor.class, () -> new ReportDataActor(getDBData));
    }

    private final GetDBData getDBData;

    public ReportDataActor(GetDBData getDBData) {
        this.getDBData = getDBData;
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AndonData.class, m -> {
                    if(m.getStartDate() == "") {
                        getDBData.getAndonData()
                                .subscribe(v -> {
                                    log.info("Andon data: {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    } else {
                        getDBData.getAndonData(m.getStartDate(), m.getEndDate())
                                .subscribe(v -> {
                                    log.info("Andon data: {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    }
                })
                .match(WorkData.class, m -> {
                    if(m.getStartDate() == "") {
                        getDBData.getWorkData()
                                .subscribe(v -> {
                                    log.info("Work data : {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    } else {
                        getDBData.getWorkData(m.getStartDate(), m.getEndDate())
                                .subscribe(v -> {
                                    log.info("Work data : {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    }
                })
                .match(ControllerData.class, m -> {
                    if(m.getStartDate() == "") {
                        getDBData.getWorkData()
                                .subscribe(v -> {
                                    log.info("Controller data : {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    } else {
                        getDBData.getWorkData(m.getStartDate(), m.getEndDate())
                                .subscribe(v -> {
                                    log.info("Controller data : {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    }
                })
                .match(ChangeParameterValueData.class, m -> {
                    if(m.getStartDate() == "") {
                        getDBData.getChangeParameterValueData()
                                .subscribe(v -> {
                                    log.info("CPV data : {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    } else {
                        getDBData.getChangeParameterValueData(m.getStartDate(), m.getEndDate())
                                .subscribe(v -> {
                                    log.info("CPV data : {}", v);
                                    getSender().tell(new Gson().toJson(v), getSelf());
                                });
                    }
                })
                .matchAny(o -> log.info("received unknown mesasage"))
                .build();
    }

    //------------------------------------------------
    //              MESSAGES CLASS
    //------------------------------------------------

    static public class AndonData {

        private final String startDate;
        private final String endDate;
        private final String stateVariableTag = "";

        public AndonData() {
            this.startDate = "";
            this.endDate = "";
        }

        public AndonData(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }

    static public class WorkData {
        private final String startDate;
        private final String endDate;
        private final String stateVariableTag = "";

        public WorkData(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public WorkData() {
            this.startDate = "";
            this.endDate = "";
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }

    static public class ControllerData {
        private final String startDate;
        private final String endDate;
        private final String stateVariableTag = "";

        public ControllerData(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public ControllerData() {
            this.startDate = "";
            this.endDate = "";
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }

    static public class ChangeParameterValueData {

        private final String startDate;
        private final String endDate;
        private final String stateVariableTag = "";

        public ChangeParameterValueData(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public ChangeParameterValueData() {
            this.startDate = "";
            this.endDate = "";
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getStateVariableTag() {
            return stateVariableTag;
        }
    }

}

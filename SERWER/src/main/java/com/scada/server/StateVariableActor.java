package com.scada.server;



import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import com.scada.dataBase.GetDBData;
import org.codehaus.jackson.map.ObjectMapper;
import rx.Observable;

import java.io.IOException;
import java.util.Map;

public class StateVariableActor extends AbstractActor {


    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(GetDBData getDBData) {
        return Props.create(StateVariableActor.class, () -> new StateVariableActor(getDBData));
    }

    private final GetDBData getDBData;

    public StateVariableActor(GetDBData getDBData) {
        this.getDBData = getDBData;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                .match(StateVariableData_dailyMode.class, m -> {
                    getDBData.getStateVariableDaily(m.getStateVariable())
                            .subscribe(v -> {
                                log.info("BAR {} data: {}", m.getStateVariable(), new Gson().toJson(v));
                                getSender().tell(v, getSelf());
                            });
                })
                .match(StateVariableData_hourlyMode.class, m -> {
                    getDBData.getStateVariableHourly(m.getStateVariable())
                            .subscribe(v -> {
                                log.info("BAR {} data: {}", m.getStateVariable(), new Gson().toJson(v));
                                getSender().tell(v, getSelf());
                            });
                })
                .match(String.class, m ->
                    getDBData.getStateSpace()
                            .subscribe(v -> {
                                log.info("SS data: {}", v);
                                getSender().tell(v, getSelf());
                            })
                )
                .match(StateVariabeData.class, m -> {
                            if (m.getEndDate() == "") {
                                getDBData.getStateSpaceData(m.getStateVariable())
                                        .subscribe(v -> {
                                            log.info("SV data: {}", new Gson().toJson(v));
                                            getSender().tell(v, getSelf());
                                        });
                            } else {
                                getDBData.getStateSpaceData(m.getStateVariable(), m.getStartDate(), m.getEndDate())
                                        .subscribe(v -> {
                                            log.info("SV data: {}", v);
                                            getSender().tell(v, getSelf());
                                        });
                            }
                        }
                )
                .match(CurrentSystemData.class, m -> {
                    getDBData.getCurrenSystemData()
                            .subscribe(v -> {
                                log.info("CV data {}", v);
                                getSender().tell(v, getSelf());
                            });
                })
                .match(LimitsData.class, m -> {
                    getDBData.getLimitsData(m.getTag())
                            .subscribe(v -> {
                                log.info("LIMITS data {}", v);
                                getSender().tell(v, getSelf());
                            });
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    //-----------------------------------------------------------
    //                      MESSAGES CLASS
    //-----------------------------------------------------------

    static public class StateVariabeData {
        private final String stateVariable;
        private final String startDate;
        private final String endDate;


        public StateVariabeData(String stateVariable, String startDate, String endDate) {
            this.stateVariable = stateVariable;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public StateVariabeData(String stateVariable) {
            this.stateVariable = stateVariable;
            this.startDate = "";
            this.endDate = "";
        }

        public String getStateVariable() {
            return stateVariable;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

    }

    static public class StateVariableData_dailyMode extends StateVariabeData {

        public StateVariableData_dailyMode(String stateVariable) {
            super(stateVariable);
        }

    }

    static public class StateVariableData_hourlyMode extends StateVariabeData {

        public StateVariableData_hourlyMode(String stateVariable) {
            super(stateVariable);
        }

    }

    static public class CurrentSystemData {}

    static public class LimitsData {
        private final String tag;

        public LimitsData(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }


}

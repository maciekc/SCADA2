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
                .match(String.class, m ->
                    getDBData.getStateSpace()
                            .subscribe(v -> {
                                log.info("SS data: {}", v);
                                getSender().tell(v, getSelf());
                            })
                )
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

}

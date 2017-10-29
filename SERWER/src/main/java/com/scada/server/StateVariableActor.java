package com.scada.server;



import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import com.scada.dataBase.GetDBData;
import rx.Observable;

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

    static public class StateVariabeData {
        private final String stateVariable;
        private final String startDate;
        private final String endDate;

        public StateVariabeData(String stateVariable, String startDate, String endDate) {
            this.stateVariable = stateVariable;
            this.startDate = startDate;
            this.endDate = endDate;
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

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StateVariabeData.class, m ->
                    getDBData.getStateSpaceData(m.getStateVariable(), m.getStartDate(), m.getEndDate())
                            .subscribe(v -> {
                                log.info("SV data: {}", v);
                                getSender().tell(new Gson().toJson(v), getSelf());
                            })
                )
                .match(String.class, m ->
                    getDBData.getStateSpace()
                            .subscribe(v -> {
                                log.info("SS data: {}", v);
                                getSender().tell(new Gson().toJson(v), getSelf());
                            })
                )
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}

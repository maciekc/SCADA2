package com.scada.dataBase;

import com.scada.model.dataBase.Limit.Limit;
import org.skife.jdbi.v2.Handle;
import rx.Observable;

public class UpdateDataInDB {

    private Handle DBhandle;
    private DBUpdates queries;

    public UpdateDataInDB(Handle DBhandle) {
        this.DBhandle = DBhandle;
        this.queries = this.DBhandle.attach(DBUpdates.class);
    }

    public Observable<Integer> updateLimit(Limit limit) {
        synchronized (this) {
            return Observable.just(queries.updateLimit(limit));
        }
    }

    public Observable<Integer> updateLimit(String stateSpaceTag, Double value, int type) {
        synchronized (this) {
            System.out.println("update " + stateSpaceTag + " " + value + " " + type);
//            return Observable.just(queries.updateLimit(stateSpaceTag, value, type));
            return Observable.fromCallable(() -> queries.updateLimit(stateSpaceTag, value, type));
        }
    }

    public  Observable<Integer> updateControllerParameter(String tag, double value) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.updateControllerPArameter(tag, value));
        }
    }

}

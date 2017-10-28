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

    public Observable<Integer> updateLimit(String tag, Double value) {
        synchronized (this) {
            return Observable.just(queries.updateLimit(tag, value));
        }
    }

}

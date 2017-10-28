package com.scada.dataBase;

import com.scada.model.dataBase.Andon.Andon;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.Limit.Limit;
import com.scada.model.dataBase.Work.Work;
import org.skife.jdbi.v2.Handle;
import rx.Observable;

public class InsertDataToDB {

    private Handle DBhandle;
    private DBUpdates queries;

    public InsertDataToDB(Handle DBhandle) {
        this.DBhandle = DBhandle;
        this.queries = this.DBhandle.attach(DBUpdates.class);
    }

    public Observable<Integer> insertAndon(Andon andon) {
        synchronized (this) {
            return Observable.just(queries.insertAndon(andon));
        }
    }

    public Observable<Integer> insertAndonToHistory(Andon andon, String type) {
        synchronized (this) {
            return Observable.just(queries.insertAndonToHistory(andon, type));
        }
    }

    public Observable<Integer> insertWork(Work work) {
        synchronized (this) {
            return Observable.just(queries.insertWork(work));
        }
    }

    public Observable<Integer> insertWorkToHistory(Work work, String type) {
        synchronized (this) {
            return Observable.just(queries.insertWorkToHistory(work, type));
        }
    }

    public Observable<Integer> insertChangeParameterValue(ChangeParameterValue cpv) {
        synchronized (this) {
            return Observable.just(queries.insertChangeParameterValue(cpv));
        }
    }

    public Observable<Integer> insertChangeParameterValueToHistory(ChangeParameterValue cpv, String type) {
        synchronized (this) {
            return Observable.just(queries.insertChangeParameterValueToHistory(cpv, type));
        }
    }

    public Observable<Integer> insertLimit(Limit limit) {
        synchronized (this) {
            return Observable.just(queries.insertLimit(limit));
        }
    }

}

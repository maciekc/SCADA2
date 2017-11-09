package com.scada.dataBase;

import com.scada.model.dataBase.Andon.Andon;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.Controller.Controller;
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

    //------------------------------------------------------------------
    //                        INSERT ANDON
    //------------------------------------------------------------------

    public Observable<Integer> insertAndon(Andon andon) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.insertAndon(andon));
        }
    }

    public Observable<Integer> insertAndonToHistory(Andon andon, String type) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.insertAndonToHistory(andon, type));
        }
    }

    public Observable<Integer> logAndonRecord(Andon andon) {
        synchronized (this) {
            return Observable.concat(this.insertAndon(andon), this.insertAndonToHistory(andon, "ANDON"));
        }
    }

    //------------------------------------------------------------------
    //                        INSERT WORK
    //------------------------------------------------------------------

    public Observable<Integer> insertWork(Work work) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.insertWork(work));
        }
    }

    public Observable<Integer> insertWorkToHistory(Work work, String type) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.insertWorkToHistory(work, type));
        }
    }

    public Observable<Integer> logWorkRecord(Work work) {
        synchronized (this) {
            return Observable.concat(this.insertWork(work), this.insertWorkToHistory(work, "WORK"));
        }
    }

    //------------------------------------------------------------------
    //                        INSERT CONTROLLER
    //------------------------------------------------------------------

    public Observable<Integer> insertController(Controller controller) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.insertController(controller));
        }
    }


    public Observable<Integer> insertControllerToHistory(Controller controller) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.insertControllerToHistory(controller));
        }
    }

    public Observable<Integer> logControllerRecord(Controller controller) {
        synchronized (this) {
            return Observable.concat(this.insertController(controller), this.insertControllerToHistory(controller));
        }
    }

    //------------------------------------------------------------------
    //                        INSERT insertChangeParameterValue
    //------------------------------------------------------------------

    public Observable<Integer> insertChangeParameterValue(ChangeParameterValue cpv) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.insertChangeParameterValue(cpv));
        }
    }

    public Observable<Integer> insertChangeParameterValueToHistory(ChangeParameterValue cpv, String type) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.insertChangeParameterValueToHistory(cpv, type));
        }
    }

    public Observable<Integer> logChangeParameterValueRecord(ChangeParameterValue cpv) {
        synchronized (this) {
            return Observable.concat(this.insertChangeParameterValue(cpv), this.insertChangeParameterValueToHistory(cpv, "CHANGE_PARAMETER_VALUE"));
        }
    }

    //------------------------------------------------------------------
    //                        INSERT LIMIT
    //------------------------------------------------------------------

    public Observable<Integer> insertLimit(Limit limit) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.insertLimit(limit));
        }
    }

}

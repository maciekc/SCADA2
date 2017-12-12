package com.scada.dataBase;

import com.scada.model.dataBase.Andon.Andon;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.Controller.Controller;
import com.scada.model.dataBase.FigurePoint.FigurePoint;
import com.scada.model.dataBase.Limit.Limit;
import com.scada.model.dataBase.Notification.Notification;
import com.scada.model.dataBase.Work.Work;
import org.skife.jdbi.v2.Handle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import rx.Observable;

public class GetDBData {

    private Handle DBhandle;
    private DBQueries queries;

    private static int maxAndonId = 0;

    public GetDBData(Handle DBhandle) {
        this.DBhandle = DBhandle;
        this.queries = this.DBhandle.attach(DBQueries.class);
    }

    //------------------------------------------------------------------
    //                       GET SS DATA
    //------------------------------------------------------------------

    public Observable<Map<String, Double>> getStateSpaceData(String stateSpace, String startDate, String endDate) {
        synchronized (this) {
//            return Observable.fromCallable(() -> queries.getStateSpaceData(stateSpace, startDate, endDate))

            return Observable.from(queries.getStateSpaceData(stateSpace, startDate, endDate))
                    .toMap(res -> res.getDate(), res -> res.getValue());
        }
    }

    public Observable<List<FigurePoint>> getStateSpaceData(String stateSpace) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getStateSpaceData(stateSpace));
        }
    }

    public Observable<List<FigurePoint>> getStateVariableDaily(String stateSpace) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getStateSpaceDataDaily(stateSpace));
        }
    }

    public Observable<List<FigurePoint>> getStateVariableHourly(String stateSpace) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getStateSpaceDataHourly(stateSpace));
        }
    }

    public Observable<Map<String, String>> getStateSpace() {
        synchronized (this) {
            return Observable.from(queries.getStateSpace())
                    .toMap(res -> res.getTag(), res -> res.getName());
        }
    }

    //------------------------------------------------------------------
    //                       GET ANDON DATA
    //------------------------------------------------------------------

    public Observable<List<Andon>> getAndonData(int startId) {
        synchronized (this) {
            Observable<List<Andon>> andonList = null;
            if (startId == -1) {
                andonList = Observable.fromCallable(() -> queries.getAndonData(-1));

            } else {
                andonList = Observable.fromCallable(() -> queries.getAndonData(maxAndonId));
            }
            andonList.forEach(v -> {for (Andon a : v) {
                this.maxAndonId = Math.max(this.maxAndonId, a.getId());
                }
            });
            return andonList;
        }
    }

    public Observable<List<Andon>> getAndonData(String startDate, String endDate) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getAndonData(startDate, endDate));
        }
    }

    //------------------------------------------------------------------
    //                       GET WORK DATA
    //------------------------------------------------------------------

    public Observable<List<Work>> getWorkData() {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getWorkData());
        }
    }

    public Observable<List<Work>> getWorkData(String startDate, String endDate) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getWorkData(startDate, endDate));
        }
    }

    //------------------------------------------------------------------
    //                       GET CONTROLLER DATA
    //------------------------------------------------------------------

    public Observable<List<Controller>> getControllerData() {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getControllerData());
        }
    }

    public Observable<List<Controller>> getControllerData(String startDate, String endDate) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getControllerData(startDate, endDate));
        }
    }

    //------------------------------------------------------------------
    //                       GET CHANGE_PARAMETER_VALUE DATA
    //------------------------------------------------------------------

    public Observable<List<ChangeParameterValue>> getChangeParameterValueData() {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getChangeParameterValueData());
        }
    }

    public Observable<List<ChangeParameterValue>> getChangeParameterValueData(String startDate, String endDate) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getChangeParameterValueData(startDate, endDate));
        }
    }

    //------------------------------------------------------------------
    //                       GET NOTIFICATION DATA
    //------------------------------------------------------------------

    public Observable<List<Notification>> getNotifications() {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getNotificationsData());
        }
    }

    public Observable<List<Notification>> getNotifications(String startDate, String endDate) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getNotificationsData(startDate, endDate));
        }
    }

    //------------------------------------------------------------------
    //                       GET CURRENT SYSTEM DATA
    //------------------------------------------------------------------

    public Observable<List<Work>> getCurrenSystemData() {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getCurrentSystemData());
        }
    }

    //------------------------------------------------------------------
    //                       GET LIMITS DATA
    //------------------------------------------------------------------

    public Observable<List<Limit>> getLimitsData(String tag) {
        synchronized (this) {
            return Observable.fromCallable(() -> queries.getLimitsData(tag));
        }
    }




//    public Observable<Map<String, Double>> getStateSpaceData() {
//        synchronized (this.DBhandle) {
//            return Observable.from(DBhandle.createQuery(DBQueries.getStateSpaceData())
//                    .map(new FigurePointMapper()).list())
//                    .toMap(res -> res.getDate(), res -> res.getValue());
//
//        }
//    }

}

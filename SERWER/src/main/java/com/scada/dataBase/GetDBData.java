package com.scada.dataBase;

import com.scada.model.dataBase.Andon.Andon;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.Controller.Controller;
import com.scada.model.dataBase.FigurePoint.FigurePoint;
import com.scada.model.dataBase.Notification.Notification;
import com.scada.model.dataBase.Work.Work;
import org.skife.jdbi.v2.Handle;

import java.util.List;
import java.util.Map;
import rx.Observable;

public class GetDBData {

    private Handle DBhandle;
    private DBQueries queries;

    public GetDBData(Handle DBhandle) {
        this.DBhandle = DBhandle;
        this.queries = this.DBhandle.attach(DBQueries.class);
    }

    //------------------------------------------------------------------
    //                       GET SS DATA
    //------------------------------------------------------------------

    public Observable<Map<String, Double>> getStateSpaceData(String stateSpace, String startDate, String endDate) {
        synchronized (this) {
            return Observable.from(queries.getStateSpaceData(stateSpace, startDate, endDate))
                    .toMap(res -> res.getDate(), res -> res.getValue());
        }
    }

    public Observable<List<FigurePoint>> getStateSpaceData(String stateSpace) {
        synchronized (this) {
            return Observable.from(queries.getStateSpaceData(stateSpace))
                    .toList();


//                    .toMap(res -> res.getDate(), res -> res.getValue());
        }
    }

    public Observable<List<FigurePoint>> getStateVariableDaily(String stateSpace) {
        synchronized (this) {
            return Observable.from(queries.getStateSpaceDataDaily(stateSpace))
                    .toList();


//                    .toMap(res -> res.getDate(), res -> res.getValue());
        }
    }

    public Observable<List<FigurePoint>> getStateVariableHourly(String stateSpace) {
        synchronized (this) {
            return Observable.from(queries.getStateSpaceDataHourly(stateSpace))
                    .toList();


//                    .toMap(res -> res.getDate(), res -> res.getValue());
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

    public Observable<Map<Integer, Andon>> getAndonData() {
        synchronized (this) {
            return Observable.from(queries.getAndonData())
                    .toMap(res -> res.getId(), res -> res);
        }
    }

    public Observable<Map<Integer, Andon>> getAndonData(String startDate, String endDate) {
        synchronized (this) {
            return Observable.from(queries.getAndonData(startDate, endDate))
                    .toMap(res -> res.getId(), res -> res);
        }
    }

    //------------------------------------------------------------------
    //                       GET WORK DATA
    //------------------------------------------------------------------

    public Observable<Map<Integer, Work>> getWorkData() {
        synchronized (this) {
            return Observable.from(queries.getWorkData())
                    .toMap(res -> res.getId(), res -> res);
        }
    }

    public Observable<Map<Integer, Work>> getWorkData(String startDate, String endDate) {
        synchronized (this) {
            return Observable.from(queries.getWorkData(startDate, endDate))
                    .toMap(res -> res.getId(), res -> res);
        }
    }

    //------------------------------------------------------------------
    //                       GET CONTROLLER DATA
    //------------------------------------------------------------------

    public Observable<Map<Integer, Controller>> getControllerData() {
        synchronized (this) {
            return Observable.from(queries.getControllerData())
                    .toMap(res -> res.getId(), res -> res);
        }
    }

    public Observable<Map<Integer, Controller>> getControllerData(String startDate, String endDate) {
        synchronized (this) {
            return Observable.from(queries.getControllerData(startDate, endDate))
                    .toMap(res -> res.getId(), res -> res);
        }
    }

    //------------------------------------------------------------------
    //                       GET CHANGE_PARAMETER_VALUE DATA
    //------------------------------------------------------------------

    public Observable<Map<Integer, ChangeParameterValue>> getChangeParameterValueData() {
        synchronized (this) {
            return Observable.from(queries.getChangeParameterValueData())
                    .toMap(res -> res.getId(), res -> res);
        }
    }

    public Observable<Map<Integer, ChangeParameterValue>> getChangeParameterValueData(String startDate, String endDate) {
        synchronized (this) {
            return Observable.from(queries.getChangeParameterValueData(startDate, endDate))
                    .toMap(res -> res.getId(), res -> res);
        }
    }

    //------------------------------------------------------------------
    //                       GET NOTIFICATION DATA
    //------------------------------------------------------------------

    public Observable<Map<Integer, Notification>> getNotifications() {
        synchronized (this) {
            return Observable.from(queries.getNotificationsData())
                    .toMap(res -> res.getId(), res -> res);
        }
    }

    public Observable<Map<Integer, Notification>> getNotifications(String startDate, String endDate) {
        synchronized (this) {
            return Observable.from(queries.getNotificationsData(startDate, endDate))
                    .toMap(res -> res.getId(), res -> res);
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

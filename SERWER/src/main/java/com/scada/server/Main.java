package com.scada.server;

import com.google.gson.Gson;
import com.scada.dataBase.DBUpdates;
import com.scada.dataBase.GetDBData;
import com.scada.dataBase.InsertDataToDB;
import com.scada.dataBase.UpdateDataInDB;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.Limit.Limit;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        final DBI dbi = new DBI("jdbc:mysql://localhost:3306/scada", "root", "1234");

        final Handle handle = dbi.open();
        final GetDBData getDBData = new GetDBData(handle);
        final InsertDataToDB insertDataToDB = new InsertDataToDB(handle);
        final UpdateDataInDB updateDataInDB = new UpdateDataInDB(handle);


        System.out.println("open bd connection");

        String query =
                "SET @stateSpaceId = (SELECT id from scada.state_space where name LIKE 'level_1');\n" +
                "SELECT date, value " +
                "FROM scada.history " +
                "WHERE state_space_id = @stateSpaceId and date between '2017-10-20 20:00:00' and '2017-10-29 20:00:00';";

//        getDBData.getStateSpaceData("LEVEL_1", "2017-10-20 14:00:00", "2017-10-29 20:00:00")
//                .subscribe(v -> System.out.println("v : " + new Gson().toJson(v)));
//
//        getDBData.getStateSpaces()
//                .subscribe(v -> System.out.println("v : " + new Gson().toJson(v)));

        System.out.println(DBUpdates.insertAndonSQL);

//        Andon andon = new Andon(1,"LEVEL_3", "LEVEL_3_MAX", 120., "2017-10-28 13:43:00");
//        rx.Observable.concat(insertDataToDB.insertAndon(andon), insertDataToDB.insertAndonToHistory(andon, "ANDON"))
//                .subscribe(v -> System.out.println("add new andon"));

//        getDBData.insertAndon(andon)
//                .subscribe(v -> System.out.println("add new andon"));
//
//        getDBData.insertHistoryRecord(new Andon(1,"LEVEL_3", "LEVEL_3_MAX", 120., "2017-10-28 13:43:00"), "ANDON")
//                .subscribe(v -> System.out.println("add new history record"));

        getDBData.getAndonData("2017-10-28 13:00:00", "2017-10-28 14:00:00")
                .subscribe(v -> System.out.println("v : " + new Gson().toJson(v)));

//        Work work = new Work(1,"LEVEL_3", 57., "2017-10-28 15:43:00");
//        rx.Observable.concat(insertDataToDB.insertWork(work), insertDataToDB.insertWorkToHistory(work, "WORK"))
//                .subscribe(v -> System.out.println("add new work"));

        ChangeParameterValue cpv= new ChangeParameterValue(1,"SET_POINT", 25., "2017-10-28 16:43:00");
        rx.Observable.concat(insertDataToDB.insertChangeParameterValue(cpv), insertDataToDB.insertChangeParameterValueToHistory(cpv, "CHANGE_PARAMETER_VALUE"))
                .subscribe(v -> System.out.println("add new cpv"));

        getDBData.getChangeParameterValueData()
                .subscribe(v -> System.out.println("CPV : " + new Gson().toJson(v)));

//        final Limit limit = new Limit(1, "LEVEL_1_MIN", "Poziom 1 min", "LEVEL_1", 2.5, -1);
//        updateDataInDB.updateLimit(limit)
//                .subscribe(v -> System.out.println("update all limit : " + new Gson().toJson(v)));
//
//        updateDataInDB.updateLimit("LEVEL_2_MIN", 2.9)
//                .subscribe(v -> System.out.println("update limit value : " + new Gson().toJson(v)));

        getDBData.getNotifications()
                .subscribe(v -> System.out.println("not : " + new Gson().toJson(v)));
        handle.close();

    }

}

package com.scada.dataBase;

import com.scada.model.dataBase.Andon.Andon;
import com.scada.model.dataBase.Andon.AndonMapper;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValueMapper;
import com.scada.model.dataBase.Controller.Controller;
import com.scada.model.dataBase.Controller.ControllerMapper;
import com.scada.model.dataBase.Controller.ControllerParameter;
import com.scada.model.dataBase.Controller.ControllerParameterMapper;
import com.scada.model.dataBase.FigurePoint.FigurePoint;
import com.scada.model.dataBase.FigurePoint.FigurePointMapper;
import com.scada.model.dataBase.Limit.Limit;
import com.scada.model.dataBase.Limit.LimitMapper;
import com.scada.model.dataBase.Notification.Notification;
import com.scada.model.dataBase.Notification.NotificationMapper;
import com.scada.model.dataBase.StateVariable.StateVariable;
import com.scada.model.dataBase.StateVariable.StateVariableMapper;
import com.scada.model.dataBase.Work.Work;
import com.scada.model.dataBase.Work.WorkMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

public interface DBQueries {

    //------------------------------------------------------------------
    //                       GET STATE SPACE DATA
    //------------------------------------------------------------------

    String getStateSpaceDate_dateRange =
            "SELECT date, value " + "\n" +
            "FROM scada.history " + "\n" +
            "WHERE state_space_id = (SELECT id from scada.state_space where tag LIKE :stateSpace) and date between :startDate and :endDate " + "\n" +
            "ORDER BY date desc " + "\n" +
            "LIMIT 100;";
    @RegisterMapper(FigurePointMapper.class)
    @SqlQuery(getStateSpaceDate_dateRange)
    List<FigurePoint> getStateSpaceData(@Bind("stateSpace") String stateSpace, @Bind("startDate") String startDate, @Bind("endDate") String endDate);

    String getStateSpaceDate =
            "SELECT date, value " + "\n" +
                    "FROM scada.history " + "\n" +
                    "WHERE state_space_id = (SELECT id from scada.state_space where tag LIKE :stateSpace) " + "\n" +
                    "ORDER BY date desc " + "\n" +
                    "LIMIT 100;";
    @RegisterMapper(FigurePointMapper.class)
    @SqlQuery(getStateSpaceDate)
    List<FigurePoint> getStateSpaceData(@Bind("stateSpace") String stateSpace);

    String getStateSpaceDataDaily =
            "SELECT sum(value) as value, date(date) as date \n" +
                    "\tFROM scada.history \n" +
                    "\tWHERE state_space_id = (SELECT id from scada.state_space where tag LIKE :stateSpace) \n" +
                    "    group by day(date), month(date)\n" +
                    "\tORDER BY date desc \n" +
                    "\tLIMIT 7;";

    @RegisterMapper(FigurePointMapper.class)
    @SqlQuery(getStateSpaceDataDaily)
    List<FigurePoint> getStateSpaceDataDaily(@Bind("stateSpace") String stateSpace);

    String getStateSpaceDataHourly =
            "SELECT sum(value) as value, min(date) as date \n" +
                    "\tFROM scada.history \n" +
                    "\tWHERE state_space_id = (SELECT id from scada.state_space where tag LIKE :stateSpace) \n" +
                    "    group by hour(date), day(date), month(date)\n" +
                    "\tORDER BY date desc \n" +
                    "\tLIMIT 24;";

    @RegisterMapper(FigurePointMapper.class)
    @SqlQuery(getStateSpaceDataHourly)
    List<FigurePoint> getStateSpaceDataHourly(@Bind("stateSpace") String stateSpace);


    String getSateSpacesQuery =
            "SELECT id, tag, name FROM scada.state_space";
    @RegisterMapper(StateVariableMapper.class)
    @SqlQuery(getSateSpacesQuery)
    List<StateVariable> getStateSpace();

    //------------------------------------------------------------------
    //                       GET ANDON DATA
    //------------------------------------------------------------------

//    String getAndonDataQuery =
//            "SELECT sa.id as id, sss.tag as stateSpace, sl.tag as limitTag, sa.value as value, sa.date as date  FROM scada.andon as sa\n" +
//            "LEFT JOIN scada.limits as sl on sa.limit_id = sl.id\n" +
//            "LEFT JOIN scada.state_space as sss on sa.state_space_id = sss.id; ";
    String getAndonDataQuery =
        "SELECT sa.id as id, sss.tag as stateSpace, sl.tag as limitTag, sa.value as value, sa.date as date, sl.type as type  FROM scada.andon as sa\n" +
                "LEFT JOIN scada.limits as sl on sa.limit_id = sl.id\n" +
                "LEFT JOIN scada.state_space as sss on sa.state_space_id = sss.id\n" +
                "WHERE sa.id > :lastId \n" +
                "ORDER BY id desc;";
    @RegisterMapper(AndonMapper.class)
    @SqlQuery(getAndonDataQuery)
    List<Andon> getAndonData(@Bind("lastId") int lastId);

    String getAndonDataQuery_dateRange =
            "SELECT sa.id as id, sss.tag as stateSpace, sl.tag as limitTag, sa.value as value, sa.date as date, sl.type as type  FROM scada.andon as sa\n" +
                    "LEFT JOIN scada.limits as sl on sa.limit_id = sl.id\n" +
                    "LEFT JOIN scada.state_space as sss on sa.state_space_id = sss.id " +
                    "WHERE sa.date BETWEEN :startDate and :endDate \n" +
                    "ORDER BY sa.id desc;";
    @RegisterMapper(AndonMapper.class)
    @SqlQuery(getAndonDataQuery_dateRange)
    List<Andon> getAndonData(@Bind("startDate") String startDate, @Bind("endDate") String endDate);

    //------------------------------------------------------------------
    //                       GET WORK DATA
    //------------------------------------------------------------------

    String getWorkDataQuery =
            "SELECT sw.id as id, sss.tag as stateSpace, sw.value as value, sw.date as date  FROM scada.work as sw\n" +
                    "LEFT JOIN scada.state_space as sss on sw.state_space_id = sss.id \n" +
                    "ORDER BY id desc \n" +
                    "LIMIT 200;";
    @RegisterMapper(WorkMapper.class)
    @SqlQuery(getWorkDataQuery)
    List<Work> getWorkData();

    String getWorkDataQuery_dateRange =
            "SELECT sw.id as id, sss.tag as stateSpace, sw.value as value, sw.date as date  FROM scada.work as sw\n" +
                    "LEFT JOIN scada.state_space as sss on sw.state_space_id = sss.id \n" +
                    "WHERE sw.date BETWEEN :startDate and :endDate;";
    @RegisterMapper(WorkMapper.class)
    @SqlQuery(getWorkDataQuery_dateRange)
    List<Work> getWorkData(@Bind("startDate") String startDate, @Bind("endDate") String endDate);

    //------------------------------------------------------------------
    //                       GET CONTROLLER DATA
    //------------------------------------------------------------------

    String getControllerDataQuery =
            "SELECT sc.id as id, sss.tag as stateSpace, sv.tag as mode, sc.value as value, sc.date as date  FROM scada.controller as sc\n" +
                    "\tLEFT JOIN scada.state_space as sss on sc.state_space_id = sss.id\n" +
                    "\tLEFT JOIN scada.variable_state as sv on sv.id = sc.variable_state_id;";
    @RegisterMapper(ControllerMapper.class)
    @SqlQuery(getControllerDataQuery)
    List<Controller> getControllerData();

    String getControllerDataQuery_dateRange =
            "SELECT sc.id as id, sss.tag as stateSpace, sv.tag as mode, sc.value as value, sc.date as date  FROM scada.controller as sc\n" +
                    "\tLEFT JOIN scada.state_space as sss on sc.state_space_id = sss.id\n" +
                    "\tLEFT JOIN scada.variable_state as sv on sv.id = sc.variable_state_id " +
                    "WHERE sc.date BETWEEN :startDate and :endDate;";
    @RegisterMapper(ControllerMapper.class)
    @SqlQuery(getControllerDataQuery_dateRange)
    List<Controller> getControllerData(@Bind("startDate") String startDate, @Bind("endDate") String endDate);

    //------------------------------------------------------------------
    //                       GET CONTROLLERS PARAMETERS DATA
    //------------------------------------------------------------------
    String query =
            "SELECT tag, value FROM scada.controller_parameter";
    @RegisterMapper(ControllerParameterMapper.class)
    @SqlQuery(query)
    List<ControllerParameter> getControllersParametersData();
    //------------------------------------------------------------------
    //                       GET CHANGE_PARAMETER_VALUE DATA
    //------------------------------------------------------------------

    String getCPVDataQuery =
            "SELECT scpv.id as id, ssp.tag as systemParameter, scpv.value as value, scpv.date as date  FROM scada.change_parameter_value as scpv\n" +
                    "LEFT JOIN scada.system_parameters as ssp on scpv.parameter_id = ssp.id \n" +
                    "ORDER BY id desc;";
    @RegisterMapper(ChangeParameterValueMapper.class)
    @SqlQuery(getCPVDataQuery)
    List<ChangeParameterValue> getChangeParameterValueData();

    String getCPVDataQuery_dateRange =
            "SELECT scpv.id as id, ssp.tag as systemParameter, scpv.value as value, scpv.date as date  FROM scada.change_parameter_value as scpv\n" +
                    "LEFT JOIN scada.system_parameters as ssp on scpv.parameter_id = ssp.id " +
                    "WHERE scpv.date BETWEEN :startDate and :endDate;";


    @RegisterMapper(ChangeParameterValueMapper.class)
    @SqlQuery(getCPVDataQuery_dateRange)
    List<ChangeParameterValue> getChangeParameterValueData(@Bind("startDate") String startDate, @Bind("endDate") String endDate);


    //------------------------------------------------------------------
    //                       GET NOTIFICATION DATA
    //------------------------------------------------------------------

    String getNotoficationDataQuery =
            "SELECT sh.id, concat_ws('',sss.tag, ssp.tag) as variableTag, sh.value, sh.date, COALESCE(sl.tag,'') as limitTag, COALESCE(sl.type, 0) as type FROM scada.history as sh\n" +
                    "\n" +
                    "LEFT JOIN scada.state_space as sss on sss.id = sh.state_space_id and sh.variable_state_id in (SELECT id FROM scada.variable_state WHERE tag = 'ANDON')\n" +
                    "LEFT JOIN scada.system_parameters as ssp on ssp.id = sh.state_space_id and sh.variable_state_id = (SELECT id FROM scada.variable_state WHERE tag = 'CHANGE_PARAMETER_VALUE')\n" +
                    "LEFT JOIN scada.limits as sl on sh.variable_state_id = (SELECT id FROM scada.variable_state WHERE tag = 'ANDON') and (SELECT limit_id FROM scada.andon WHERE id = sh.event_id) = sl.id\n" +
                    "\n" +
                    "WHERE sh.variable_state_id in (SELECT id FROM scada.variable_state WHERE tag = 'ANDON' or tag = 'CHANGE_PARAMETER_VALUE')\n" +
                    "ORDER BY id desc; ";
    @RegisterMapper(NotificationMapper.class)
    @SqlQuery(getNotoficationDataQuery)
    List<Notification> getNotificationsData();


    String getNotificationDataQuery_dateRange =
            "SELECT sh.id, concat_ws('',sss.tag, ssp.tag) as variableTag, sh.value, sh.date, COALESCE(sl.tag,'') as limitTag, COALESCE(sl.type, 0) as type FROM scada.history as sh\n" +
                    "\n" +
                    "LEFT JOIN scada.state_space as sss on sss.id = sh.state_space_id and sh.variable_state_id in (SELECT id FROM scada.variable_state WHERE tag = 'ANDON')\n" +
                    "LEFT JOIN scada.system_parameters as ssp on ssp.id = sh.state_space_id and sh.variable_state_id = (SELECT id FROM scada.variable_state WHERE tag = 'CHANGE_PARAMETER_VALUE')\n" +
                    "LEFT JOIN scada.limits as sl on sh.variable_state_id = (SELECT id FROM scada.variable_state WHERE tag = 'ANDON') and (SELECT limit_id FROM scada.andon WHERE id = sh.event_id) = sl.id\n" +
                    "\n" +
                    "WHERE (date BETWEEN :startDate and :endDate) and (sh.variable_state_id in (SELECT id FROM scada.variable_state WHERE tag = 'ANDON' or tag = 'CHANGE_PARAMETER_VALUE'))\n" +
                    "ORDER BY id desc; ";


    @RegisterMapper(NotificationMapper.class)
    @SqlQuery(getNotificationDataQuery_dateRange)
    List<Notification> getNotificationsData(@Bind("startDate") String startDate, @Bind("endDate") String endDate);


    //------------------------------------------------------------------
    //                       GET CURRENT SYSTEM DATA
    //------------------------------------------------------------------

    String getCurrentSystemData =
            "(SELECT sw.id, sss.tag as stateSpace, sw.value, sw.date FROM scada.work as sw\n" +
                    "LEFT JOIN scada.state_space as sss ON sss.id = sw.state_space_id\n" +
                    "order by sw.id desc\n" +
                    "LIMIT 4)\n" +
            "union\n" +
            "(SELECT sc.id, sss.tag, sc.value as stateSpace, sc.date FROM scada.controller as sc\n" +
                    "LEFT JOIN scada.state_space as sss ON sss.id = sc.state_space_id\n" +
                    "order by sc.id desc\n" +
                    "LIMIT 4);";
    @RegisterMapper(WorkMapper.class)
    @SqlQuery(getCurrentSystemData)
    List<Work> getCurrentSystemData ();


    //------------------------------------------------------------------
    //                       GET LIMITS DATA
    //------------------------------------------------------------------

    String getLimitsData =
            "SELECT id, tag, name, state_space_id as stateSpaceName, value, type FROM scada.limits" + "\n" +
                    "WHERE state_space_id = (SELECT id FROM scada.state_space WHERE tag LIKE :stateVariableTag);";
    @RegisterMapper(LimitMapper.class)
    @SqlQuery(getLimitsData)
    List<Limit> getLimitsData (@Bind("stateVariableTag") String tag);









//    public  static  String getStateSpaceData() {
//        return "SELECT date, value " + "\n" +
//            "FROM scada.history " + "\n" +
//            "WHERE state_space_id = (SELECT id from scada.state_space where name LIKE 'level_1') and date between '2017-10-20 20:00:00' and '2017-10-29 20:00:00';";
//    }
}

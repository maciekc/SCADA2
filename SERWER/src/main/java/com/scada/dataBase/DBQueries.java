package com.scada.dataBase;

import com.scada.model.dataBase.Andon.Andon;
import com.scada.model.dataBase.Andon.AndonMapper;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValueMapper;
import com.scada.model.dataBase.FigurePoint.FigurePoint;
import com.scada.model.dataBase.FigurePoint.FigurePointMapper;
import com.scada.model.dataBase.Notification.Notification;
import com.scada.model.dataBase.Notification.NotificationMapper;
import com.scada.model.dataBase.StateSpace.StateSpace;
import com.scada.model.dataBase.StateSpace.StateSpaceMapper;
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

    String getStateSpaceDate =
            "SELECT date, value " + "\n" +
            "FROM scada.history " + "\n" +
            "WHERE state_space_id = (SELECT id from scada.state_space where tag LIKE :stateSpace) and date between :startDate and :endDate;";
    @RegisterMapper(FigurePointMapper.class)
    @SqlQuery(getStateSpaceDate)
    List<FigurePoint> getStateSpaceData(@Bind("stateSpace") String stateSpace, @Bind("startDate") String startDate, @Bind("endDate") String endDate);


    String getSateSpacesQuery =
            "SELECT id, name FROM scada.state_space";
    @RegisterMapper(StateSpaceMapper.class)
    @SqlQuery(getSateSpacesQuery)
    List<StateSpace> getStateSpaces();

    //------------------------------------------------------------------
    //                       GET ANDON DATA
    //------------------------------------------------------------------

    String getAndonDataQuery =
            "SELECT sa.id as id, sss.name as stateSpace, sl.name as limitName, sa.value as value, sa.date as date  FROM scada.andon as sa\n" +
            "LEFT JOIN scada.limits as sl on sa.limit_id = sl.id\n" +
            "LEFT JOIN scada.state_space as sss on sa.state_space_id = sss.id; ";
    @RegisterMapper(AndonMapper.class)
    @SqlQuery(getAndonDataQuery)
    List<Andon> getAndonData();

    String getAndonDataQuery_dateRange =
            "SELECT sa.id as id, sss.name as stateSpace, sl.name as limitName, sa.value as value, sa.date as date  FROM scada.andon as sa\n" +
                    "LEFT JOIN scada.limits as sl on sa.limit_id = sl.id\n" +
                    "LEFT JOIN scada.state_space as sss on sa.state_space_id = sss.id " +
                    "WHERE sa.date BETWEEN :startDate and :endDate;";
    @RegisterMapper(AndonMapper.class)
    @SqlQuery(getAndonDataQuery_dateRange)
    List<Andon> getAndonData(@Bind("startDate") String startDate, @Bind("endDate") String endDate);

    //------------------------------------------------------------------
    //                       GET WORK DATA
    //------------------------------------------------------------------

    String getWorkDataQuery =
            "SELECT sw.id as id, sss.name as stateSpace, sw.value as value, sw.date as date  FROM scada.work as sw\n" +
                    "LEFT JOIN scada.state_space as sss on sw.state_space_id = sss.id;";
    @RegisterMapper(WorkMapper.class)
    @SqlQuery(getWorkDataQuery)
    List<Work> getWorkData();

    String getWorkDataQuery_dateRange =
            "SELECT sw.id as id, sss.name as stateSpace, sw.value as value, sw.date as date  FROM scada.work as sw\n" +
                    "LEFT JOIN scada.state_space as sss on sw.state_space_id = sss.id " +
                    "WHERE sw.date BETWEEN :startDate and :endDate;";
    @RegisterMapper(WorkMapper.class)
    @SqlQuery(getWorkDataQuery_dateRange)
    List<Work> getWorkData(@Bind("startDate") String startDate, @Bind("endDate") String endDate);

    //------------------------------------------------------------------
    //                       GET CHANGE_PARAMETER_VALUE DATA
    //------------------------------------------------------------------

    String getCPVDataQuery =
            "SELECT scpv.id as id, ssp.name as systemParameter, scpv.value as value, scpv.date as date  FROM scada.change_parameter_value as scpv\n" +
                    "LEFT JOIN scada.system_parameters as ssp on scpv.parameter_id = ssp.id; ";
    @RegisterMapper(ChangeParameterValueMapper.class)
    @SqlQuery(getCPVDataQuery)
    List<ChangeParameterValue> getChangeParameterValueData();

    String getCPVDataQuery_dateRange =
            "SELECT scpv.id as id, ssp.name as systemParameter, scpv.value as value, scpv.date as date  FROM scada.change_parameter_value as scpv\n" +
                    "LEFT JOIN scada.system_parameters as ssp on scpv.parameter_id = ssp.id " +
                    "WHERE scpv.date BETWEEN :startDate and :endDate;";


    @RegisterMapper(ChangeParameterValueMapper.class)
    @SqlQuery(getCPVDataQuery_dateRange)
    List<ChangeParameterValue> getChangeParameterValueData(@Bind("startDate") String startDate, @Bind("endDate") String endDate);


    //------------------------------------------------------------------
    //                       GET NOTIFICATION DATA
    //------------------------------------------------------------------

    String getNotoficationDataQuery =
            "SELECT sh.id, concat_ws('',sss.name, ssp.name) as name, sh.value, sh.date, COALESCE(sl.name,'') as limit_name, COALESCE(sl.type, 0) as type FROM scada.history as sh\n" +
                    "\n" +
                    "LEFT JOIN scada.state_space as sss on sss.id = sh.state_space_id and sh.variable_state_id in (SELECT id FROM scada.variable_state WHERE tag = 'ANDON')\n" +
                    "LEFT JOIN scada.system_parameters as ssp on ssp.id = sh.state_space_id and sh.variable_state_id = (SELECT id FROM scada.variable_state WHERE tag = 'CHANGE_PARAMETER_VALUE')\n" +
                    "LEFT JOIN scada.limits as sl on sh.variable_state_id = (SELECT id FROM scada.variable_state WHERE tag = 'ANDON') and (SELECT limit_id FROM scada.andon WHERE id = sh.event_id) = sl.id\n" +
                    "\n" +
                    "WHERE sh.variable_state_id in (SELECT id FROM scada.variable_state WHERE tag = 'ANDON' or tag = 'CHANGE_PARAMETER_VALUE')\n" +
                    "ORDER BY date asc; ";
    @RegisterMapper(NotificationMapper.class)
    @SqlQuery(getNotoficationDataQuery)
    List<Notification> getNotificationsData();


    String getNotificationDataQuery_dateRange =
            "SELECT sh.id, concat_ws('',sss.name, ssp.name) as name, sh.value, sh.date, COALESCE(sl.name,'') as limit_name, COALESCE(sl.type, 0) as type FROM scada.history as sh\n" +
                    "\n" +
                    "LEFT JOIN scada.state_space as sss on sss.id = sh.state_space_id and sh.variable_state_id in (SELECT id FROM scada.variable_state WHERE tag = 'ANDON')\n" +
                    "LEFT JOIN scada.system_parameters as ssp on ssp.id = sh.state_space_id and sh.variable_state_id = (SELECT id FROM scada.variable_state WHERE tag = 'CHANGE_PARAMETER_VALUE')\n" +
                    "LEFT JOIN scada.limits as sl on sh.variable_state_id = (SELECT id FROM scada.variable_state WHERE tag = 'ANDON') and (SELECT limit_id FROM scada.andon WHERE id = sh.event_id) = sl.id\n" +
                    "\n" +
                    "WHERE (date BETWEEN :startDate and :endDate) and (sh.variable_state_id in (SELECT id FROM scada.variable_state WHERE tag = 'ANDON' or tag = 'CHANGE_PARAMETER_VALUE'))\n" +
                    "ORDER BY date asc; ";


    @RegisterMapper(NotificationMapper.class)
    @SqlQuery(getCPVDataQuery_dateRange)
    List<Notification> getNotificationsData(@Bind("startDate") String startDate, @Bind("endDate") String endDate);










//    public  static  String getStateSpaceData() {
//        return "SELECT date, value " + "\n" +
//            "FROM scada.history " + "\n" +
//            "WHERE state_space_id = (SELECT id from scada.state_space where name LIKE 'level_1') and date between '2017-10-20 20:00:00' and '2017-10-29 20:00:00';";
//    }
}

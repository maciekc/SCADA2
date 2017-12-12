package com.scada.dataBase;

import com.scada.model.dataBase.Andon.Andon;
import com.scada.model.dataBase.Andon.AndonBinder;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValue;
import com.scada.model.dataBase.ChangeParameterValue.ChangeParameterValueBinder;
import com.scada.model.dataBase.Controller.Controller;
import com.scada.model.dataBase.Controller.ControllerBinder;
import com.scada.model.dataBase.Limit.Limit;
import com.scada.model.dataBase.Limit.LimitBinder;
import com.scada.model.dataBase.Work.Work;
import com.scada.model.dataBase.Work.WorkBinder;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface DBUpdates {

    //------------------------------------------------------------------
    //                        INSERT ANDON
    //------------------------------------------------------------------

    String insertAndonSQL =
            "INSERT INTO scada.andon\n" +
                    "(\n" +
                    "limit_id,\n" +
                    "state_space_id,\n" +
                    "value,\n" +
                    "date)\n" +
                    "VALUES\n" +
                    "((SELECT id FROM limits WHERE tag LIKE :limitTag), " +
                    "(SELECT state_space_id FROM limits WHERE tag LIKE :limitTag), " +
                    ":value, " +
                    ":date);";

    //    @RegisterMapper(AndonMapper.class)
    @SqlUpdate(insertAndonSQL)
    int insertAndon(@AndonBinder Andon andon);

    String insertAndonToHistorySQL =
            "INSERT INTO scada.history \n" +
                    "(variable_state_id,\n" +
                    "state_space_id,\n" +
                    "event_id,\n" +
                    "value,\n" +
                    "date)\n" +
                "VALUES\n" +
                    "((SELECT id FROM scada.variable_state WHERE tag LIKE :var_state), " +
                    "(SELECT id FROM limits WHERE tag LIKE :limitTag), " +
                    "(SELECT max(id) FROM scada.andon), " +
                    ":value, " +
                    ":date);";
    @SqlUpdate(insertAndonToHistorySQL)
    int insertAndonToHistory(@AndonBinder Andon andon, @Bind("var_state") String var_state);

    //------------------------------------------------------------------
    //                        INSERT WORK
    //------------------------------------------------------------------

    String insertWorkSQL =
            "INSERT INTO scada.work\n" +
                    "(\n" +
                    "state_space_id,\n" +
                    "value,\n" +
                    "date)\n" +
                "VALUES\n" +
                    "((SELECT id FROM state_space WHERE tag LIKE :stateSpaceTag), " +
                    ":value, " +
                    ":date);";

    @SqlUpdate(insertWorkSQL)
    int insertWork(@WorkBinder Work work);


    String insertWorkToHistorySQL =
            "INSERT INTO scada.history \n" +
                    "(variable_state_id,\n" +
                    "state_space_id,\n" +
                    "event_id,\n" +
                    "value,\n" +
                    "date)\n" +
                    "VALUES\n" +
                    "((SELECT id FROM scada.variable_state WHERE tag LIKE :var_state), " +
                    "(SELECT id FROM scada.state_space WHERE tag LIKE :stateSpaceTag), " +
                    "(SELECT max(id) FROM scada.work), " +
                    ":value, " +
                    ":date);";
    @SqlUpdate(insertWorkToHistorySQL)
    int insertWorkToHistory(@WorkBinder Work work, @Bind("var_state") String var_state);



    //------------------------------------------------------------------
    //                        INSERT CONTROLLER
    //------------------------------------------------------------------

    String insertControllerSQL =
            "INSERT INTO scada.controller\n" +
                    "(\n" +
                    "state_space_id,\n" +
                    "variable_state_id,\n" +
                    "value,\n" +
                    "date)\n" +
                    "VALUES\n" +
                    "((SELECT id FROM state_space WHERE tag LIKE :stateSpaceTag), " +
                    "(SELECT id FROM scada.variable_state WHERE tag LIKE :variableStateTag), " +
                    ":value, " +
                    ":date);";

    @SqlUpdate(insertControllerSQL)
    int insertController(@ControllerBinder Controller controller);


    String insertControllerToHistorySQL =
            "INSERT INTO scada.history \n" +
                    "(variable_state_id,\n" +
                    "state_space_id,\n" +
                    "event_id,\n" +
                    "value,\n" +
                    "date)\n" +
                    "VALUES\n" +
                    "((SELECT id FROM scada.variable_state WHERE tag LIKE :variableStateTag), " +
                    "(SELECT id FROM scada.state_space WHERE tag LIKE :stateSpaceTag), " +
                    "(SELECT max(id) FROM scada.controller), " +
                    ":value, " +
                    ":date);";
    @SqlUpdate(insertControllerToHistorySQL)
    int insertControllerToHistory(@ControllerBinder Controller controller);

    //------------------------------------------------------------------
    //                 INSERT CHANGE_PARAMETER_VALUE DATA
    //------------------------------------------------------------------

    String insertCPVSQL =
            "INSERT INTO scada.change_parameter_value\n" +
                    "(\n" +
                    "parameter_id,\n" +
                    "value,\n" +
                    "date)\n" +
                    "VALUES\n" +
                    "((SELECT id FROM scada.system_parameters WHERE tag LIKE :systemParameterTag), " +
                    ":value, " +
                    ":date);";

    @SqlUpdate(insertCPVSQL)
    int insertChangeParameterValue(@ChangeParameterValueBinder ChangeParameterValue cpv);


    String insertCPVToHistorySQL =
            "INSERT INTO scada.history \n" +
                    "(variable_state_id,\n" +
                    "state_space_id,\n" +
                    "event_id,\n" +
                    "value,\n" +
                    "date)\n" +
                    "VALUES\n" +
                    "((SELECT id FROM scada.variable_state WHERE tag LIKE :var_state), " +
                    "(SELECT id FROM scada.system_parameters WHERE tag LIKE :systemParameterTag), " +
                    "(SELECT max(id) FROM scada.change_parameter_value), " +
                    ":value, " +
                    ":date);";
    @SqlUpdate(insertCPVToHistorySQL)
    int insertChangeParameterValueToHistory(@ChangeParameterValueBinder ChangeParameterValue cpv, @Bind("var_state") String var_state);

    //------------------------------------------------------------------
    //                          UPDATE LIMIT VALUE
    //------------------------------------------------------------------

    String updateLimitSQL =
            "UPDATE scada.limits\n" +
                    "SET\n" +
                    "name = :name,\n" +
                    "state_space_id = (SELECT id FROM scada.state_space WHERE tag LIKE :stateSpaceTag),\n" +
                    "value = :value,\n" +
                    "WHERE tag LIKE :tag;";

    @SqlUpdate(updateLimitSQL)
    int updateLimit(@LimitBinder Limit limit);

    String updateLimitValueSQL =
            "UPDATE scada.limits\n" +
                    "SET\n" +
                    "value = :value\n" +
                    "WHERE tag = :limitTag;";

    @SqlUpdate(updateLimitValueSQL)
    int updateLimit(@Bind("limitTag") String limitTag, @Bind("value") Double value, @Bind("type") int type);

    String insertLimitSQL =
        "INSERT INTO `scada`.`limits`\n"+
                "(\n"+
                "tag`,\n"+
                "name`,\n"+
                "state_space_id`,\n"+
                "value`,\n"+
                "type`)\n"+
                "VALUES\n"+
                "(\n"+
                ":tag,\n"+
                ":name,\n"+
                "(SELECT id FROM scada.state_space WHERE tag LIKE :stateSpaceTag),\n"+
                ":value,\n"+
                ":type);";

    @SqlUpdate(insertLimitSQL)
    int insertLimit(@LimitBinder Limit limit);

}

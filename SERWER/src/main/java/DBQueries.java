import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import java.util.List;
import java.util.Map;

public interface DBQueries {

//    @SqlQuery("SELECT date, value " + "\n" +
//            "FROM scada.history " + "\n" +
//            "WHERE state_space_id = (SELECT id from scada.state_space where name LIKE 'level_1') and date between '2017-10-20 20:00:00' and '2017-10-29 20:00:00';")
//    List<Map<String, Double>> getStateSpaceData();

//    List<Map<String, Double>> getStateSpaceData(@Bind("stateSpace") String stateSpace, @Bind("startDate") String startDate, @Bind("endDate") String endDate);


    public  static  String getStateSpaceData() {
        return "SELECT date, value " + "\n" +
            "FROM scada.history " + "\n" +
            "WHERE state_space_id = (SELECT id from scada.state_space where name LIKE 'level_1') and date between '2017-10-20 20:00:00' and '2017-10-29 20:00:00';";
    }
}

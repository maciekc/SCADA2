import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.StringMapper;
import rx.Observable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        final DBI dbi = new DBI("jdbc:mysql://localhost:3306/scada", "root", "1234");

        final Handle handle = dbi.open();
        System.out.println("open bd connection");

        String query = "SET @stateSpaceId = (SELECT id from scada.state_space where name LIKE 'level_1');\n" +
                                "SELECT date, value " +
                                "FROM scada.history " +
                                "WHERE state_space_id = @stateSpaceId and date between '2017-10-20 20:00:00' and '2017-10-29 20:00:00';";
//        query = "SELECT date, value " + "\n" +
//                "FROM scada.history " + "\n" +
//                "WHERE state_space_id = (SELECT id from scada.state_space where name LIKE 'level_1') and date between '2017-10-20 20:00:00' and '2017-10-29 20:00:00';";
//
//        final List<Map<String, Object>> db_name = handle.createQuery(query).list();
//
//        System.out.println(db_name);

        final GetDBData getDBData = new GetDBData(handle);

//
//        getDBData.getStateSpaceData("level_1", "2017-10-20 20:00:00", "2017-10-29 20:00:00")

        getDBData.getStateSpaceData()
                .subscribe(v -> System.out.println("v : " + v));

        handle.close();


    }

}

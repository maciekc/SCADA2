import org.skife.jdbi.v2.Handle;

import java.util.HashMap;
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

//    public Observable<Map<String, Double>> getStateSpaceData(String stateSpace, String startDate, String endDate) {
//        synchronized (this.DBhandle) {
//            return Observable.from(queries.getStateSpaceData())
//                    .toMap(res -> res.keySet().iterator().next(), res -> res.values().iterator().next());
//        }
//    }


    public Observable<Map<String, Double>> getStateSpaceData() {
        synchronized (this.DBhandle) {
            return Observable.from(DBhandle.createQuery(DBQueries.getStateSpaceData())
                    .map(new FigurePointMapper()).list())
                    .toMap(res -> res.getDate(), res -> res.getValue());

        }
    }



//    public Observable<Map<String, Double>> getStateSpaceData(String stateSpace, String startDate, String endDate) {
//        synchronized (this.DBhandle) {
//            return Observable.from(queries.getStateSpaceData(stateSpace, startDate, endDate))
//                    .toMap(res -> res.keySet().iterator().next(), res -> res.values().iterator().next());
//        }
//    }
}

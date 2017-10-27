import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FigurePointMapper implements ResultSetMapper<FigurePoint> {

    @Override
    public FigurePoint map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        FigurePoint figurePoint = new FigurePoint();
        figurePoint.setDate(resultSet.getString("date"));
        figurePoint.setValue(resultSet.getDouble("value"));
        return figurePoint;
    }
}
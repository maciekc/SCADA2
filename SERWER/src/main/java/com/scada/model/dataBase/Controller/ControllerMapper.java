package com.scada.model.dataBase.Controller;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerMapper implements ResultSetMapper<Controller> {

    @Override
    public Controller map(int i, ResultSet rs, StatementContext statementContext) throws SQLException {
        return new Controller(rs.getInt("id"), rs.getString("stateSpace"), rs.getString("mode"), rs.getDouble("value"), rs.getString("date"));
    }
}

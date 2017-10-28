package com.scada.model.dataBase.Work;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkMapper implements ResultSetMapper<Work>{

    @Override
    public Work map(int i, ResultSet rs, StatementContext statementContext) throws SQLException {
        return new Work(rs.getInt("id"), rs.getString("stateSpace"), rs.getDouble("value"), rs.getString("date"));
    }
}

package com.scada.model.dataBase.ChangeParameterValue;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangeParameterValueMapper implements ResultSetMapper<ChangeParameterValue> {

    @Override
    public ChangeParameterValue map(int i, ResultSet rs, StatementContext statementContext) throws SQLException {
        return new ChangeParameterValue(rs.getInt("id"), rs.getString("systemParameter"), rs.getDouble("value"), rs.getString("date"));
    }
}

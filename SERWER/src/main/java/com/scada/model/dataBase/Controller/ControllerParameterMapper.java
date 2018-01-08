package com.scada.model.dataBase.Controller;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerParameterMapper implements ResultSetMapper<ControllerParameter> {

    @Override
    public ControllerParameter map(int i, ResultSet rs, StatementContext statementContext) throws SQLException {
        return new ControllerParameter(rs.getString("tag"), rs.getDouble("value"));
    }
}

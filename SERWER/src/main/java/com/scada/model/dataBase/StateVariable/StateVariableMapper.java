package com.scada.model.dataBase.StateVariable;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StateVariableMapper implements ResultSetMapper<StateVariable>{

    @Override
    public StateVariable map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new StateVariable(resultSet.getInt("id"), resultSet.getString("tag"), resultSet.getString("name"));
    }
}

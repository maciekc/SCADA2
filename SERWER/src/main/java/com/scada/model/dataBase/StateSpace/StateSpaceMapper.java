package com.scada.model.dataBase.StateSpace;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StateSpaceMapper implements ResultSetMapper<StateSpace>{

    @Override
    public StateSpace map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new StateSpace(resultSet.getInt("id"), resultSet.getString("name"));
    }
}

package com.scada.model.dataBase.Andon;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AndonMapper implements ResultSetMapper<Andon> {

    @Override
    public Andon map(int i, ResultSet rs, StatementContext statementContext) throws SQLException {
        return new Andon(rs.getInt("id"), rs.getString("stateSpace"), rs.getString("limitName"), rs.getDouble("value"), rs.getString("date"));
    }
}

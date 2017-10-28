package com.scada.model.dataBase.Limit;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LimitMapper implements ResultSetMapper<Limit> {

    @Override
    public Limit map(int i, ResultSet rs, StatementContext statementContext) throws SQLException {
        return new Limit(rs.getInt("id"), rs.getString("tag"), rs.getString("name"), rs.getString("stateSpaceName"), rs.getDouble("value"), rs.getInt("type"));
    }
}

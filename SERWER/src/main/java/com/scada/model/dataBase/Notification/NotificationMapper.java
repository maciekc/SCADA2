package com.scada.model.dataBase.Notification;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationMapper implements ResultSetMapper<Notification> {

    @Override
    public Notification map(int i, ResultSet rs, StatementContext statementContext) throws SQLException {
        return new Notification(rs.getInt("id"), rs.getString("variableTag"), rs.getString("limitTag"), rs.getDouble("value"), rs.getInt("type"), rs.getString("date"));
    }
}

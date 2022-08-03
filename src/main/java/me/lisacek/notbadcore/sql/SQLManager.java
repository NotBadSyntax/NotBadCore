package me.lisacek.notbadcore.sql;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class SQLManager {

    private final List<DatabaseConnection> connections = new ArrayList<>();

    public DatabaseConnection createConnection(JsonObject info, boolean readOnly) {
        ConnectionInfo connectionInfo = ConnectionInfo.load(info);

        if (connectionInfo == null) {
            return null;
        }

        DatabaseConnection connection = new DatabaseConnection(connectionInfo);
        try {
            connection.connect();
        } catch (IllegalStateException e) {
            return null;
        }
        connections.add(connection);
        return connection;
    }

    public void close() {
        connections.forEach(DatabaseConnection::close);
    }

}
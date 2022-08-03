package me.lisacek.notbadcore.sql;

import com.google.gson.JsonObject;
import jdk.internal.net.http.common.Pair;

import java.util.HashMap;
import java.util.Map;

public class SQLManager {

    private static final SQLManager INSTANCE = new SQLManager();

    private final Map<String, DatabaseConnection> connections = new HashMap<>();

    public Pair<String, DatabaseConnection> createConnection(JsonObject info, boolean readOnly) {
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
        String uuid = java.util.UUID.randomUUID().toString();
        connections.put(uuid, connection);
        return new Pair<>(uuid, connection);
    }

    public void close() {
        connections.values().forEach(DatabaseConnection::close);
    }

    public DatabaseConnection getConnection(String uuid) {
        return connections.get(uuid);
    }

    public static SQLManager getInstance() {
        return INSTANCE;
    }
}
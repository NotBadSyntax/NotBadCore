package me.lisacek.notbadcore.sql;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

public class ConnectionInfo {

    @Getter
    private final String host;
    @Getter
    private final int port;
    @Getter
    private final String user;
    @Getter
    private final String password;
    @Getter
    private final String database;

    @Getter
    @Setter
    private boolean readOnly = false;

    public ConnectionInfo(String host, int port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public static ConnectionInfo load(JsonObject config) {
        if (config == null)
            return null;

        ConnectionInfo info = new ConnectionInfo(
                config.get("host").getAsString(),
                config.get("port").getAsInt(),
                config.get("username").getAsString(),
                config.get("password").getAsString(),
                config.get("database").getAsString());
        info.setReadOnly(false);
        return info;
    }

    @Override
    public String toString() {
        return user + "@" + host + ":" + port + "/" + database;
    }
}

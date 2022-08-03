package me.lisacek.notbadcore.sql;

import com.zaxxer.hikari.HikariDataSource;
import jdk.internal.joptsimple.internal.Strings;
import lombok.Getter;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class DatabaseConnection {

    @Getter
    private final ConnectionInfo info;

    private HikariDataSource hikari;

    @Getter
    private boolean connected = false;

    public DatabaseConnection(ConnectionInfo info) {
        this.info = info;
    }

    public void connect() {
        this.hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");

        hikari.addDataSourceProperty("serverName", info.getHost());
        hikari.addDataSourceProperty("port", info.getPort());
        hikari.addDataSourceProperty("databaseName", info.getDatabase());

        hikari.addDataSourceProperty("user", info.getUser());
        hikari.addDataSourceProperty("password", info.getPassword());
        hikari.addDataSourceProperty("characterEncoding", "utf8");
        try {
            hikari.validate();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        }

        Connection connection;
        try {
            connection = hikari.getConnection();
        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        }

        if (connection == null)
            throw new IllegalStateException("No connection");

        this.connected = true;
    }

    public CompletableFuture<Boolean> execute(String query, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> {

            if (!isConnected())
                return false;

            try (Connection connection = hikari.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                if (parameters != null) {
                    for (int i = 0; i < parameters.length; i++) {
                        statement.setObject(i + 1, parameters[i]);
                    }
                }

                statement.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletableFuture<ResultSet> executeQuery(String query, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> {
            if (!isConnected())
                return null;

            try (Connection connection = hikari.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                if (parameters != null) {
                    for (int i = 0; i < parameters.length; i++) {
                        statement.setObject(i + 1, parameters[i]);
                    }
                }

                CachedRowSet resultCached = RowSetProvider.newFactory().createCachedRowSet();
                ResultSet resultSet = statement.executeQuery();

                resultCached.populate(resultSet);
                resultSet.close();

                return resultCached;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //throw new NullPointerException();
            return null;
        });
    }

    public void close() {
        if (hikari != null)
            hikari.close();
    }
}
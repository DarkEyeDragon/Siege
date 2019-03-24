package me.darkeyedragon.siege.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

class DataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    static final String TABLE_NAME = "islands";
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    static{
        config.setJdbcUrl(JDBC_URL+TABLE_NAME);
        //TODO implement config for database settings
        config.setUsername("root");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    static Connection getConnection() throws SQLException{
        return ds.getConnection();
    }
}

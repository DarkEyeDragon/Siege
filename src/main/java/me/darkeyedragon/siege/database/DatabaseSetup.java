package me.darkeyedragon.siege.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class DatabaseSetup {

    public static boolean databaseExists() throws SQLException {
        String existsQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'guilds'";
        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(existsQuery);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.first();
        }
    }

    /**
     * Create the database tables. It is expected for the database to exist. Otherwise the plugin will exit.
     * @throws SQLException
     */
    public static void createTables() throws SQLException {

        String guild = ""
                + "CREATE TABLE IF NOT EXISTS `guild` ( "
                + "  `id` int(16) NOT NULL AUTO_INCREMENT PRIMARY KEY , "
                + "  `name` varchar(16) NOT NULL UNIQUE , "
                + "  `member` varchar(32) NOT NULL "
                + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";


        String guildMember = ""
                + "CREATE TABLE IF NOT EXISTS `guild_member` ( "
                + "  `uuid` binary(16) NOT NULL PRIMARY KEY , "
                + "  `guild_id` int(32) NOT NULL, "
                + "  `role` varchar(16) NOT NULL "
                + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";


        String user = ""
                + "CREATE TABLE IF NOT EXISTS `user` ( "
                + "  `uuid` binary(16) NOT NULL PRIMARY KEY , "
                + "  `first_join` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP , "
                + "  `last_join` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";


        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(guild);
            statement.execute();
            statement = connection.prepareStatement(guildMember);
            statement.execute();
            statement = connection.prepareStatement(user);
            statement.execute();
        }
    }
}

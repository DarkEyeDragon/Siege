package me.darkeyedragon.siege.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSetup {

    public static boolean databaseExists() throws SQLException {
        String existsQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'guilds'";
        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(existsQuery);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.first();
        }
    }

    /***
     *
     * @throws SQLException thrown when the tables can't be created.
     * Likely caused by insufficient access privileges or non existent database
     */
    public static void createTables() throws SQLException {

        String guild = ""
                + "CREATE TABLE IF NOT EXISTS `guild` ( "
                + "  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY , "
                + "  `name` varchar(16) NOT NULL UNIQUE, "
                + "  `balance` BIGINT DEFAULT 0, "
                + "  `date_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

        String user = ""
                + "CREATE TABLE IF NOT EXISTS `user` ( "
                + "  `uuid` binary(16) NOT NULL PRIMARY KEY , "
                + "  `first_join` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP , "
                + "  `last_join` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

        String guildMember = ""
                + "CREATE TABLE IF NOT EXISTS `guild_member` ( "
                + "  `uuid` binary(16) NOT NULL PRIMARY KEY , "
                + "  `guild_id` int(11) NOT NULL, "
                + "  `role` varchar(16) NOT NULL,"
                + " FOREIGN KEY (uuid) REFERENCES user(uuid),"
                + " FOREIGN KEY (guild_id) REFERENCES guild(id)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";


        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(guild);
            statement.execute();
            statement = connection.prepareStatement(user);
            statement.execute();
            statement = connection.prepareStatement(guildMember);
            statement.execute();

        }
    }
}

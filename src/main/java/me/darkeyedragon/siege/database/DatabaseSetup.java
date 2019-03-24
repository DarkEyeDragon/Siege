package me.darkeyedragon.siege.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSetup {

    public static boolean databaseExists() throws SQLException {
        String existsQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'islands'";
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

        String island = ""
                + "CREATE TABLE IF NOT EXISTS `island` ( "
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

        String islandMember = ""
                + "CREATE TABLE IF NOT EXISTS `island_member` ( "
                + "  `uuid` binary(16) NOT NULL PRIMARY KEY , "
                + "  `island_id` int(11) NOT NULL, "
                + "  `role` varchar(16) NOT NULL,"
                + " FOREIGN KEY (uuid) REFERENCES user(uuid),"
                + " FOREIGN KEY (island_id) REFERENCES island(id)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

        String invites = ""
                + "CREATE TABLE IF NOT EXISTS `invites` ( "
                + "  `id` int(11) NOT NULL PRIMARY KEY , "
                + "  `fromPlayer` BINARY(11) NOT NULL, "
                + "  `toPlayer` BINARY(16) NOT NULL,"
                + " FOREIGN KEY (fromPlayer) REFERENCES user(uuid),"
                + " FOREIGN KEY (toPlayer) REFERENCES user(uuid) "
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";


        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(island);
            statement.execute();
            statement = connection.prepareStatement(user);
            statement.execute();
            statement = connection.prepareStatement(islandMember);
            statement.execute();
            statement = connection.prepareStatement(invites);
            statement.execute();

        }
    }
}

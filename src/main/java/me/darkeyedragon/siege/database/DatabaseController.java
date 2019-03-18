package me.darkeyedragon.siege.database;

import me.darkeyedragon.siege.Siege;
import me.darkeyedragon.siege.guild.Guild;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DatabaseController {

    public static boolean insertPlayer(Player player) {
        String insertPlayerQuery = "INSERT INTO USER (UUID) VALUES (?);";
        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(insertPlayerQuery);
            statement.setString(1, player.getUniqueId().toString());
            executeAsync(statement);
        } catch (SQLException ex) {
            return false;
        }
        return false;
    }

    private static void executeAsync(PreparedStatement preparedStatement) {
        Siege.getExecutorService().submit(() -> {
            try {
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
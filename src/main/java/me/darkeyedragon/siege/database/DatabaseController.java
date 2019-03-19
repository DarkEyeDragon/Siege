package me.darkeyedragon.siege.database;

import me.darkeyedragon.siege.Siege;
import me.darkeyedragon.siege.util.UUIDConverter;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class DatabaseController {

    public static void insertPlayer(Player player, Logger logger) {
        CompletableFuture.supplyAsync(() -> {
            String insertPlayerQuery = "INSERT INTO user (UUID) VALUES (?) ON DUPLICATE KEY UPDATE last_join= CURRENT_TIMESTAMP;";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(insertPlayerQuery);
                statement.setBytes(1, UUIDConverter.toBytes(player.getUniqueId()));
                logger.info("Updated data for "+player.getName());
                return statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
                logger.info("Unable to update data for "+player.getName());
                return false;
            }
        }, Siege.getExecutorService());
    }
}
package me.darkeyedragon.siege.database;

import me.darkeyedragon.siege.Siege;
import me.darkeyedragon.siege.guild.Island;
import me.darkeyedragon.siege.util.UUIDConverter;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class DatabaseController {

    public static void insertPlayer(Player player, Logger logger) {
        CompletableFuture.supplyAsync(() -> {
            String insertPlayerQuery = "INSERT INTO user (UUID) VALUES (?) ON DUPLICATE KEY UPDATE last_join= CURRENT_TIMESTAMP;";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(insertPlayerQuery);
                statement.setBytes(1, UUIDConverter.toBytes(player.getUniqueId()));
                logger.info("Updated data for " + player.getName());
                return statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
                logger.info("Unable to update data for " + player.getName());
                return false;
            }
        }, Siege.getExecutorService());
    }

    public static CompletableFuture<Boolean> insertGuild(Island island, Logger logger) {
        return CompletableFuture.supplyAsync(() -> {
            String insertGuildQuery = "INSERT INTO guild (name) VALUES (?);";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(insertGuildQuery);
                statement.setString(1, island.getName());
                logger.info(island.getOwner().getName() + " created new island named " + island.getName());
                statement.execute();
                return true;
            } catch (SQLException ex) {
                logger.info("Unable to create island for " + island.getOwner().getName());
                return false;
            }
        }, Siege.getExecutorService());
    }

    public static CompletableFuture<Boolean> isInGuild(Player player) {
        return isInGuild(player.getUniqueId());
    }

    public static CompletableFuture<Boolean> isInGuild(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String containsGuildQuery = "SELECT * FROM guild_member WHERE uuid=?";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(containsGuildQuery);
                statement.setBytes(1, UUIDConverter.toBytes(uuid));
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }, Siege.getExecutorService());
    }


    //TODO setup proper lists with all fields.
    public static CompletableFuture<List<String>> getGuilds() {
        return CompletableFuture.supplyAsync(() -> {
            String containsGuildQuery = "SELECT name FROM guild";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(containsGuildQuery);
                ResultSet resultSet = statement.executeQuery();
                List<String> guilds = new ArrayList<>();
                while(resultSet.next()){
                    guilds.add(resultSet.getString("name"));
                }
                return guilds;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
        }, Siege.getExecutorService());
    }
    /*public static CompletableFuture<Boolean> addGuildMember(String guildName, Player player, Logger logger) {
        return CompletableFuture.supplyAsync(() -> {
            String insertGuildQuery = "INSERT INTO guild_member (uuid, guild_id) VALUES ( ? ,(SELECT id FROM guild WHERE name=?));";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(insertGuildQuery);
                statement.setString(1, guildName);
                logger.info(player.getName() + " created a new guild named  " + guildName);
                return statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
                logger.info("Unable to create guild for " + guild.getOwner());
                return false;
            }
        }, Siege.getExecutorService());
    }*/
}
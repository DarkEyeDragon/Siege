package me.darkeyedragon.siege.database;

import me.darkeyedragon.siege.Siege;
import me.darkeyedragon.siege.island.Island;
import me.darkeyedragon.siege.island.Rank;
import me.darkeyedragon.siege.util.UUIDConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseController {

    public static void insertPlayer(Player player) {
        CompletableFuture.supplyAsync(() -> {
            String insertPlayerQuery = "INSERT INTO user (UUID) VALUES (?) ON DUPLICATE KEY UPDATE last_join= CURRENT_TIMESTAMP;";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(insertPlayerQuery);
                statement.setBytes(1, UUIDConverter.toBytes(player.getUniqueId()));
                Bukkit.getLogger().info("Updated data for " + player.getName());
                return statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Bukkit.getLogger().info("Unable to update data for " + player.getName());
                return false;
            }
        }, Siege.getExecutorService());
    }


    public static CompletableFuture<Boolean> removePlayer(Player player) {
        CompletableFuture<Island> islandFuture = getIsland(player);
        return islandFuture.thenApply((island -> {
            String removePlayerQuery = "DELETE FROM island_member where uuid=? AND island_id=?";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(removePlayerQuery);
                statement.setBytes(1, UUIDConverter.toBytes(player.getUniqueId()));
                statement.setInt(2, island.getId());
                statement.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getLogger().warning("Unable to remove player from island!");
                return false;
            }
        }));
    }

    public static CompletableFuture<Boolean> insertIsland(Island island) {
        return CompletableFuture.supplyAsync(() -> {
            String insertIslandQuery = "INSERT IGNORE INTO island (name) VALUES (?);";
            String insertIslandOwner = "INSERT INTO island_member(uuid, island_id, role) values(?,LAST_INSERT_ID(), ?) ;";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(insertIslandQuery);
                statement.setString(1, island.getName());
                Bukkit.getLogger().info(island.getOwner().getName() + " created new island named " + island.getName());
                statement = connection.prepareStatement(insertIslandOwner);
                statement.setBytes(1, UUIDConverter.toBytes(island.getOwner().getUniqueId()));
                statement.setString(2, Rank.KING.toString());
                statement.execute();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Bukkit.getLogger().info("Unable to create island for " + island.getOwner().getName());
                return false;
            }
        }, Siege.getExecutorService());
    }

    public static CompletableFuture<Boolean> isInIsland(Player player) {
        return isInIsland(player.getUniqueId());
    }

    public static CompletableFuture<Boolean> isInIsland(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String containsIslandQuery = "SELECT * FROM island_member WHERE uuid=?";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(containsIslandQuery);
                statement.setBytes(1, UUIDConverter.toBytes(uuid));
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next();
            } catch (SQLException ex) {
                Bukkit.getLogger().warning("Unable to look up if " + uuid + " is in an island!");
                ex.printStackTrace();
                return false;
            }
        }, Siege.getExecutorService());
    }


    //TODO setup proper lists with all fields.
    public static CompletableFuture<List<String>> getIslands() {
        return CompletableFuture.supplyAsync(() -> {
            String containsIslandQuery = "SELECT name FROM island";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(containsIslandQuery);
                ResultSet resultSet = statement.executeQuery();
                List<String> islands = new ArrayList<>();
                while (resultSet.next()) {
                    islands.add(resultSet.getString("name"));
                }
                return islands;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
        }, Siege.getExecutorService());
    }


    public static CompletableFuture<Island> getIsland(Player player) {
        return getIsland(player.getUniqueId());
    }

    public static CompletableFuture<Island> getIsland(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String containsIslandQuery = "SELECT * FROM island WHERE id IN(select island_id from island_member where uuid=?)";
            String getIslandMembers = "SELECT uuid FROM island_member where island_id=?";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(containsIslandQuery);
                statement.setBytes(1, UUIDConverter.toBytes(uuid));
                ResultSet islandSet = statement.executeQuery();
                if (islandSet.next()) {
                    Island island = new Island();
                    statement = connection.prepareStatement(getIslandMembers);
                    statement.setInt(1, islandSet.getInt("id"));
                    ResultSet memberSet = statement.executeQuery();
                    if (memberSet.next()) {
                        UUID uuidMember = UUIDConverter.fromBytes(memberSet.getBytes("uuid"));
                        island.setId(islandSet.getInt("id"));
                        island.getMembers().add(uuidMember);
                        island.setName(islandSet.getString("name"));
                    }
                    return island;
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Bukkit.getLogger().warning("Unable to get island for user " + Bukkit.getPlayer(uuid));
                return null;
            }
        }, Siege.getExecutorService());
    }

    public static CompletableFuture<Boolean> addIslandMember(String islandName, Player player, Rank rank) {
        return CompletableFuture.supplyAsync(() -> {
            String insertIslandQuery = "INSERT INTO island_member (uuid, island_id, role) VALUES ( ? ,(SELECT id FROM island WHERE name=?), ?);";
            try (Connection connection = DataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(insertIslandQuery);
                statement.setBytes(1, UUIDConverter.toBytes(player.getUniqueId()));
                statement.setString(1, islandName);
                statement.setString(1, rank.toString());
                Bukkit.getLogger().info(player.getName() + " has been added to " + islandName);
                statement.execute();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Bukkit.getLogger().info("Unable to create island for " + player.getName());
                return false;
            }
        }, Siege.getExecutorService());
    }
}
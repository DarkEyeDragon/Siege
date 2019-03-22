package me.darkeyedragon.siege;

import co.aikar.commands.PaperCommandManager;
import me.darkeyedragon.siege.command.IslandCommand;
import me.darkeyedragon.siege.command.SiegeCommand;
import me.darkeyedragon.siege.database.DatabaseSetup;
import me.darkeyedragon.siege.event.PlayerJoin;
import me.darkeyedragon.siege.event.PlayerLeave;
import me.darkeyedragon.siege.guild.Island;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Siege extends JavaPlugin {

    private static ExecutorService executorService;
    private static HashSet<Island> islands;

    private PaperCommandManager commandManager;

    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {
        islands = new HashSet<>();
        executorService = Executors.newFixedThreadPool(10);
        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new SiegeCommand());
        commandManager.registerCommand(new IslandCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(this), this);
        try {
            if(DatabaseSetup.databaseExists()){
                getLogger().info("Database found. Proceeding...");
                DatabaseSetup.createTables();
            }else{
                getLogger().info("Database not found. Shutting down.");
                this.getPluginLoader().disablePlugin(this);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Can't contact database. Disabling siege...");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public static HashSet<Island> getIslands() {
        return islands;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }


}

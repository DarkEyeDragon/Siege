package me.darkeyedragon.siege;

import co.aikar.commands.PaperCommandManager;
import me.darkeyedragon.siege.command.GuildCommand;
import me.darkeyedragon.siege.command.SiegeCommand;
import me.darkeyedragon.siege.database.DatabaseSetup;
import me.darkeyedragon.siege.event.PlayerJoin;
import me.darkeyedragon.siege.guild.Guild;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Siege extends JavaPlugin {

    private static ExecutorService executorService;
    private static HashSet<Guild> guilds;

    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        guilds = new HashSet<>();
        executorService = Executors.newFixedThreadPool(10);
        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new SiegeCommand());
        commandManager.registerCommand(new GuildCommand());
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
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
            getLogger().severe("We done fucked up bois. Shutting down to prevent corruption...");
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

    public static HashSet<Guild> getGuilds() {
        return guilds;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }
}

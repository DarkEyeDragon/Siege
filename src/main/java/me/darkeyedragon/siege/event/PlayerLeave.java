package me.darkeyedragon.siege.event;

import me.darkeyedragon.siege.database.DatabaseController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerLeave implements Listener {
    private JavaPlugin plugin;

    public PlayerLeave(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        DatabaseController.insertPlayer(event.getPlayer(), plugin.getLogger());
    }
}

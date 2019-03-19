package me.darkeyedragon.siege.event;

import me.darkeyedragon.siege.database.DatabaseController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerJoin implements Listener {

    private JavaPlugin plugin;

    public PlayerJoin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        DatabaseController.insertPlayer(event.getPlayer(), plugin.getLogger());
    }
}

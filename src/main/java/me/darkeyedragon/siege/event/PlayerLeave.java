package me.darkeyedragon.siege.event;

import me.darkeyedragon.siege.database.DatabaseController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        DatabaseController.insertPlayer(event.getPlayer());
    }
}

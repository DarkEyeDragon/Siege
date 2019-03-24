package me.darkeyedragon.siege.event;

import me.darkeyedragon.siege.database.DatabaseController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        DatabaseController.insertPlayer(event.getPlayer());
    }
}

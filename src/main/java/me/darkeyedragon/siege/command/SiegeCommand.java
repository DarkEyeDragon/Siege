package me.darkeyedragon.siege.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.destroystokyo.paper.Title;
import me.darkeyedragon.siege.gamemode.SiegeMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandAlias("siege")
public class SiegeCommand extends BaseCommand {

    @Default
    @Subcommand("start")
    @Description("Start a siege event. Either as attacker or defender")
    public void startSiege(Player player, String[] args) {
        if (args.length > 0) {
            SiegeMode mode = SiegeMode.getMode(args[0].toLowerCase());
            if (mode != null) {
                player.sendTitle(new Title(ChatColor.GREEN+ "Added to queue", ChatColor.AQUA+"You will be playing as "+mode.toString()));
            }
        }
    }
}

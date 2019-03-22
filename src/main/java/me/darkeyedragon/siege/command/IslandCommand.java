package me.darkeyedragon.siege.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import me.darkeyedragon.siege.database.DatabaseController;
import me.darkeyedragon.siege.guild.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@CommandAlias("guild")
public class IslandCommand extends BaseCommand {

    private JavaPlugin plugin;

    public IslandCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Subcommand("list")
    @Description("List all islands")
    public void list(Player player, @Single int page){
        CompletableFuture<List<String>> guildsFuture = DatabaseController.getGuilds();
        guildsFuture.thenAccept(list->{
            int pages = 1+(int)Math.ceil(list.size()/18);
            player.sendMessage(ChatColor.YELLOW+"======= "+ChatColor.AQUA+" Guilds (1/"+pages+")"+ChatColor.YELLOW+" =======");
            for (String s : list) {
                player.sendMessage(s);
            }
        });
    }

    @Subcommand("create")
    @Description("Create an island")
    public void createGuild(Player player,@Single String name) {
        CompletableFuture<Boolean> future = DatabaseController.isInGuild(player);
        future.thenAccept(inGuild -> {
            if (!inGuild) {
                Island island = new Island();
                island.setOwner(player);
                island.setName(name);
                CompletableFuture<Boolean> created = DatabaseController.insertGuild(island, plugin.getLogger());
                created.thenAccept(isCreated -> {
                    if (isCreated) {
                        player.sendMessage(ChatColor.GREEN + "Your island has been created.");
                    } else {
                        player.sendMessage(
                                ChatColor.RED + "An island with that name already exists!");
                    }
                });
            } else {
                player.sendMessage(
                        ChatColor.RED + "You already are in a guild! Leave first before leaving");
            }
        });
    }

    @Subcommand("invite")
    @Description("Invite an online player to your island. ")
    public void invite(Player player, String target) {
        Player targetP = Bukkit.getPlayer(target);
        //targetP.sendMessage("You have been invited to join "+guildName);
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}

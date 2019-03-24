package me.darkeyedragon.siege.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import me.darkeyedragon.siege.database.DatabaseController;
import me.darkeyedragon.siege.island.Island;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@CommandAlias("island")
public class IslandCommand extends BaseCommand {

    @Subcommand("list")
    @Description("List all islands")
    public void list(Player player, @Optional String[] args) {
        CompletableFuture<List<String>> islandFuture = DatabaseController.getIslands();
        islandFuture.thenAccept(list -> {
            int pages = 1 + (int) Math.ceil(list.size() / 18);
            player.sendMessage(ChatColor.YELLOW + "======= " + ChatColor.AQUA + " Islands (1/" + pages + ")" + ChatColor.YELLOW + " =======");
            for (String s : list) {
                player.sendMessage(s);
            }
        });
    }

    @Subcommand("create")
    @Description("Create an island")
    public void createIsland(Player player, @Single String name) {
        CompletableFuture<Boolean> future = DatabaseController.isInIsland(player);
        future.thenAccept(inIsland -> {
            if (!inIsland) {
                Island island = new Island();
                island.setOwner(player);
                island.setName(name);
                CompletableFuture<Boolean> created = DatabaseController.insertIsland(island);
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
                        ChatColor.RED + "You already are in a island! Leave first before leaving");
            }
        });
    }

    @Subcommand("invite")
    @Description("Invite an online player to your island.")
    public void invite(Player player, Player target) {
        CompletableFuture<Island> island = DatabaseController.getIsland(player.getUniqueId());
        if(player.getName().equalsIgnoreCase(target.getName())){
            player.sendMessage(ChatColor.RED+"You can't invite yourself you silly goose!");
            return;
        }
        island.thenAccept(islandObj -> {
            if (islandObj != null) {
                player.sendMessage(ChatColor.GREEN + "You have invited " + target.getName() + " to join " + islandObj.getName());
                target.sendMessage(ChatColor.GREEN + "You have been invited to join " + islandObj.getName());
            } else {
                player.sendMessage(ChatColor.RED + "You can't invite while not in a guild!");
            }
        });
    }

    @Subcommand("kick")
    @Description("Kick a player from your island")
    public void kick(Player player, Player target){

    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}

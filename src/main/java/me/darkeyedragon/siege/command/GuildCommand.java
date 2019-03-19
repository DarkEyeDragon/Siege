package me.darkeyedragon.siege.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import me.darkeyedragon.siege.database.DatabaseController;
import me.darkeyedragon.siege.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@CommandAlias("guild")
public class GuildCommand extends BaseCommand {

    private JavaPlugin plugin;

    public GuildCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Subcommand("list")
    @Description("List all guilds")
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
    @Description("Create a guild")
    public void createGuild(Player player,@Single String name) {
        CompletableFuture<Boolean> future = DatabaseController.isInGuild(player);
        future.thenAccept(inGuild -> {
            if (!inGuild) {
                Guild guild = new Guild();
                guild.setOwner(player);
                guild.setName(name);
                CompletableFuture<Boolean> created = DatabaseController.insertGuild(guild, plugin.getLogger());
                created.thenAccept(isCreated -> {
                    if (isCreated) {
                        player.sendMessage(ChatColor.GREEN + "Your guild has been created.");
                    } else {
                        player.sendMessage(
                                ChatColor.RED + "A guild with that name already exists!");
                    }
                });
            } else {
                player.sendMessage(
                        ChatColor.RED + "You already are in a guild! Leave first before leaving");
            }
        });
    }

    @Subcommand("invite")
    @Description("Invite an online player to your guild. ")
    public void invite(Player player, String target) {
        Player targetP = Bukkit.getPlayer(target);
        //targetP.sendMessage("You have been invited to join "+guildName);
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}

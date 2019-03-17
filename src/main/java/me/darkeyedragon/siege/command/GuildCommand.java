package me.darkeyedragon.siege.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.destroystokyo.paper.Title;
import me.darkeyedragon.siege.Siege;
import me.darkeyedragon.siege.gamemode.SiegeMode;
import me.darkeyedragon.siege.guild.Guild;
import me.darkeyedragon.siege.guild.GuildMember;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("guild")
public class GuildCommand extends BaseCommand {

    @Subcommand("create")
    @Description("Create a guild")
    public void createGuild(Player player, String name) {
        for (Guild guild : Siege.getGuilds()) {
            if (guild.getMembers().containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are already in a guild!");
                return;
            }
        }
        Optional<Guild> guildFinder = Siege.getGuilds().stream().filter(guild -> name.equalsIgnoreCase(guild.getGuildName())).findFirst();
        if (guildFinder.isPresent()) {
            player.sendMessage(ChatColor.RED + "This guild already exists");
            return;
        }
        GuildMember guildMember = new GuildMember();
        guildMember.setRank("king");
        guildMember.setUuid(player.getUniqueId());
        Guild guild = new Guild(name, player);
        guild.addMember(guildMember);
        Siege.getGuilds().add(guild);
        player.sendMessage(ChatColor.GREEN + "You have created the guild successfully. You are now king of " + name);
    }

    @Subcommand("invite")
    @Description("Invite an online player to your guild. ")
    public void invite(Player player, String target){
        Player targetP = Bukkit.getPlayer(target);
        //targetP.sendMessage("You have been invited to join "+guildName);
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}

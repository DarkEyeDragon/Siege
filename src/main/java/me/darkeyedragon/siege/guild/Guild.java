package me.darkeyedragon.siege.guild;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class Guild implements IDatabaseSerializable {
    private Map<UUID, GuildMember> members;
    private StringBuilder stringBuilder;
    private String guildName;
    private HashSet<String> ranks;
    public Guild(String guildName, Player owner) {
        stringBuilder = new StringBuilder();
        members = new HashMap<>();
        this.guildName = guildName;
    }

    public String getMembersAsString() {
        members.forEach((key, value) -> {
            stringBuilder.append(value);
            stringBuilder.append(", ");
        });
        return stringBuilder.toString();
    }

    public RankStatus setRank(Player player, String rank){
        UUID id = player.getUniqueId();
        if(members.containsKey(id)){
            GuildMember member = members.get(id);
            if(ranks.contains(rank)) {
                member.setRank(rank);
                return RankStatus.SUCCESS;
            }
            return RankStatus.INVALID_RANK;
        }
        return RankStatus.INVALID_USER;
    }

    public Map<UUID, GuildMember> getMembers() {
        return members;
    }

    public String getGuildName() {
        return guildName;
    }

    public HashSet<String> getRanks() {
        return ranks;
    }

    public void addMember(GuildMember guildMember){
        members.put(guildMember.getUuid(), guildMember);
    }

    @Override
    public Map<String, Object> serializeObject() {
        Map<String, Object> data = new HashMap<>();
        //data.put("", )
        return null;
    }
}

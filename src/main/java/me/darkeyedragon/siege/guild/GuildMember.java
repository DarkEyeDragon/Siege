package me.darkeyedragon.siege.guild;

import java.util.UUID;

public class GuildMember {
    private UUID uuid;
    private String rank;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}

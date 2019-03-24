package me.darkeyedragon.siege.island;

public class GuildList {
    private int id;
    private String name;
    private int balance;

    public GuildList(int id, String name, int balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public GuildList() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}

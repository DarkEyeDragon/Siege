package me.darkeyedragon.siege.gamemode;

public enum SiegeMode {
    ATTACKER, DEFENDER, RANDOM;

    public static SiegeMode getMode(String string){
        for (SiegeMode mode : SiegeMode.values()){
            if(mode.toString().equalsIgnoreCase(string)){
                return mode;
            }
        }
        return null;
    }
}

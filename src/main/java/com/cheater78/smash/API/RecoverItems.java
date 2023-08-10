package com.cheater78.smash.API;

import org.bukkit.entity.Player;

public interface RecoverItems {

    //Save Items of Player
    void onJoin(Player p);

    //Restore Items
    void onLeave(Player p);

}

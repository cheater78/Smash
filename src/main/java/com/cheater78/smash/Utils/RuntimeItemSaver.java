package com.cheater78.smash.Utils;

import com.cheater78.smash.API.RecoverItems;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RuntimeItemSaver implements RecoverItems {

    public Map<UUID, GameMode> playerModes;
    public Map<UUID, Float> playerLevel;
    public Map<UUID,Map<Integer,ItemStack>> playerInvs;

    public RuntimeItemSaver(){
        playerModes = new HashMap<>();
        playerLevel = new HashMap<>();
        playerInvs = new HashMap<>();
    }

    @Override
    public void onJoin(Player p) {
        Map<Integer, ItemStack> inv = new HashMap<>();
        for(int i = 0; i <= 45; i++)
            inv.put(i, p.getInventory().getItem(i));
        playerInvs.put(p.getUniqueId(), inv);
        playerLevel.put(p.getUniqueId(), p.getExp()+p.getLevel());
        playerModes.put(p.getUniqueId(),p.getGameMode());
    }

    @Override
    public void onLeave(Player p) {
        p.setExp(playerLevel.get(p.getUniqueId()) % 1);
        p.setLevel((int) playerLevel.get(p.getUniqueId()).floatValue());
        for(int i = 0; i < playerInvs.get(p.getUniqueId()).size(); i++){
            p.getInventory().setItem(i, playerInvs.get(p.getUniqueId()).get(i));
        }
        p.setGameMode(playerModes.get(p.getUniqueId()));
    }
}

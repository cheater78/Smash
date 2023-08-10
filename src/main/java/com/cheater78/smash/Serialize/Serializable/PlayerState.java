package com.cheater78.smash.Serialize.Serializable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class PlayerState implements Serializable {

    private UUID uuid;

    @Override
    public UUID getUuid() {
        return uuid;
    }

    class ItemStack{

        class ItemMeta{

        }

        int count;
        int material;
        List<Integer> enchantments;

        public ItemStack(org.bukkit.inventory.ItemStack iStack){
            count = iStack.getAmount();
            material = iStack.getType();
        }

    }

    private int playerLvl;
    private float playerExp;

    List<ItemStack> playerItems;

    public PlayerState(Player p){
        playerExp = p.getExp();
        playerLvl = p.getLevel();



    }

    public int getPlayerLvl() { return playerLvl; }
    public float getPlayerExp() { return playerExp; }

}

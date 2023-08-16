package com.cheater78.smash.Game.Elements;

import com.cheater78.smash.Serialize.Serializable.Serializable;
import com.cheater78.smash.Smash;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public abstract class Item implements Listener, Serializable {

    public static final Enchantment ENA = Enchantment.PROTECTION_ENVIRONMENTAL;
    private static final boolean showEnch = false;

    static final ArrayList<String> iTag = (ArrayList<String>) Arrays.asList(
            ChatColor.DARK_GRAY + "Smash by c78"
    );

    private UUID uuid;

    private String name;
    private ItemStack iStack;
    private int pickupAmount = 1;

    public Item(String name, Material material){
        if(name == null) throw new NullPointerException();
        if(name.isEmpty()) throw new IllegalArgumentException();

        this.name = name;

        iStack = new ItemStack(material);
        iStack.setAmount(1);
        iStack.addUnsafeEnchantment(ENA, 10);

        ItemMeta iMeta = iStack.getItemMeta();
        if(iMeta == null) throw new NullPointerException();
        if(!showEnch)
            iMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iMeta.setDisplayName(name);
        iMeta.setLore(iTag);
        iMeta.setUnbreakable(true);
        iStack.setItemMeta(iMeta);

        this.uuid = new UUID(name.hashCode(), name.hashCode());
    }

    public ItemStack get(boolean enchanted){
        if(enchanted)
            iStack.addUnsafeEnchantment(ENA, 10);
        else
            iStack.removeEnchantment(ENA);
        return iStack;
    }

    public String getName(){ return name; }

    public int getPickupAmount() {
        return pickupAmount;
    }
    public void setPickupAmount(int pickupAmount) {
        this.pickupAmount = pickupAmount;
    }

    public int getOccurence() {
        if(iStack.getAmount() > 1)
            return iStack.getAmount();
        else
            if(iStack.containsEnchantment(ENA))
                return 1;
            else
                return 0;

    }
    public void setOccurence(int occurence) {
        if(occurence == 0){
            iStack.removeEnchantment(ENA);
            iStack.setAmount(0);
        }else{
            iStack.addUnsafeEnchantment(ENA, 10);
            iStack.setAmount(occurence);
        }
    }

    @Override
    public UUID getUuid() {
        return new UUID(getName().hashCode(), getName().hashCode());
    }
}

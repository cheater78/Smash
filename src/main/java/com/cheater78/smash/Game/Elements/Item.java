package com.cheater78.smash.Game.Elements;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class Item {

    public static final Enchantment ENA = Enchantment.PROTECTION_ENVIRONMENTAL;
    static final boolean showEnch = false;

    static final ArrayList<String> iTag = (ArrayList<String>) Arrays.asList(
            ChatColor.DARK_GRAY + "Smash by c78"
    );

    String name;
    ItemStack iStack;

    public Item(String name, Material material){
        if(name == null) throw new NullPointerException();
        if(name.equals("")) throw new IllegalArgumentException();

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
    }

    public ItemStack get(boolean enchanted){
        if(enchanted)
            iStack.addUnsafeEnchantment(ENA, 10);
        else
            iStack.removeEnchantment(ENA);
        return iStack;
    }

    public String getName(){ return name; }

}

package com.cheater78.smash.Game.Elements.GUI;

import com.cheater78.smash.Config.PluginConfig;
import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Elements.ItemManager;
import com.cheater78.smash.Game.Elements.Items;
import com.cheater78.smash.Game.SmashGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryGUI {

    public static void setLobbyItems(Player p){
        if (PluginConfig.useLobbyItems){
            p.getInventory().clear();
            Inventory inv = p.getInventory();

            ItemStack leave = new ItemStack(Material.BARRIER);
            ItemMeta mleave = leave.getItemMeta();
            mleave.setDisplayName(ChatColor.RED+"MainLobby");
            leave.setItemMeta(mleave);

            ItemStack settings = new ItemStack(Material.CHEST);
            ItemMeta msettings = settings.getItemMeta();
            msettings.setDisplayName(ChatColor.GREEN+ "Settings");
            settings.setItemMeta(msettings);

            ItemStack start = new ItemStack(Material.NETHER_STAR);
            ItemMeta mstart = start.getItemMeta();
            mstart.setDisplayName(ChatColor.AQUA + "Start");
            start.setItemMeta(mstart);

            inv.setItem(3,start);
            inv.setItem(4,settings);
            inv.setItem(5,leave);

            p.getInventory().setHeldItemSlot(4);
        }
    }

    public void settingsGUI(Player p, SmashGame game){
        Inventory inv = Bukkit.createInventory(p, 54, "Smash Match Settings (" + game.getArena().getName() + ")");
        List<Item> guiItems = Items.getAll();

        for(int i = 0; i < guiItems.size(); i++){
            int count = game.getItemManager().getItemOccurence(guiItems.get(i));
            ItemStack is = guiItems.get(i).get( count > 0 );
            if(count > 1)
                is.setAmount(count);
            inv.setItem(28+(i%7) + 9*(i/7), is);
        }

        ItemStack lives = new ItemStack(Material.RED_DYE);
        ItemMeta mlives = lives.getItemMeta();
        mlives.setDisplayName(ChatColor.RED + "Lives: " + ChatColor.GREEN + game.getLives());
        lives.setItemMeta(mlives);
        inv.setItem(11, lives);

        ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta mexp = exp.getItemMeta();
        mexp.setDisplayName(ChatColor.GREEN + "MaxDamage: " + ChatColor.YELLOW + game.getMaxExp());
        exp.setItemMeta(mexp);
        inv.setItem(15, exp);

        ItemStack cancel = new ItemStack(Material.BARRIER);
        ItemMeta mcancel = cancel.getItemMeta();
        mcancel.setDisplayName(ChatColor.GRAY + "Exit");
        cancel.setItemMeta(mcancel);
        inv.setItem(13, cancel);

        p.openInventory(inv);
    }

}

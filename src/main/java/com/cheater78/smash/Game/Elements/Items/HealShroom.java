package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Systems.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class HealShroom extends Item {

    private final int heal = 50;

    public HealShroom() {
        super(ChatColor.DARK_RED +       "H"
                + ChatColor.RED +           "e"
                + ChatColor.GOLD +          "a"
                + ChatColor.YELLOW +        "l"
                + ChatColor.GREEN+          "S"
                + ChatColor.DARK_AQUA +     "h"
                + ChatColor.BLUE +          "r"
                + ChatColor.LIGHT_PURPLE +  "o"
                + ChatColor.DARK_PURPLE +   "o"
                + ChatColor.DARK_RED +      "m",            Material.RED_MUSHROOM);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!PlayerManager.isInGame(p)) return;
        if (!p.getInventory().getItemInMainHand().isSimilar(this.get(false))) return;

        if(p.getLevel() <= heal){
            p.setLevel(0);
        }else{
            p.setLevel(p.getLevel()-heal);
        }
        p.getInventory().setItemInMainHand(null);
        e.setCancelled(true);

        p.playSound(p.getEyeLocation(), Sound.ENTITY_PLAYER_BURP, 0.5f, 1);
    }
}

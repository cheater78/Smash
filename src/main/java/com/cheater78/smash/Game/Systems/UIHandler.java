package com.cheater78.smash.Game.Systems;

import com.cheater78.smash.Smash;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class UIHandler implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (Arena.arenaOfPlayer(p, true) != null) {
        } else if (Arena.arenaOfPlayer(p, false) != null) {
            Arena a = Arena.arenaOfPlayer(p, false);
            if (p.getInventory().getItemInMainHand() != null) {
                ItemStack i = p.getInventory().getItemInMainHand();
                if (Smash.useLobbyItems && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                    if (i.getType() == Material.NETHER_STAR) {
                        p.performCommand("smash start");
                    }
                    if (i.getType() == Material.CHEST) {
                        p.performCommand("smash settings");
                    }
                    if (i.getType() == Material.BARRIER) {
                        p.performCommand("smash leave");
                    }
                }
            }
        }
    }

}

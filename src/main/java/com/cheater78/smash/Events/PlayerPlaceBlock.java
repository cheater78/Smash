package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Items.Items;
import com.cheater78.smash.Smash;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlayerPlaceBlock implements Listener {

    public static Plugin plugin;

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();

        if(Arena.arenaOfPlayer(p,true) != null){
            if(p.getInventory().getItemInMainHand().isSimilar(Items.tnt())){
                p.getInventory().setItemInMainHand(null);
                e.setCancelled(true);

                ArmorStand tnt = (ArmorStand) p.getWorld().spawnEntity(e.getBlock().getLocation().add(0.5,-1.2,0.5), EntityType.ARMOR_STAND);
                tnt.setInvisible(true);
                tnt.setGravity(false);
                tnt.setHelmet(new ItemStack(Material.TNT));
                tnt.setCustomNameVisible(true);
                tnt.setCustomName(ChatColor.GREEN + "3");

                Smash.sced.scheduleSyncDelayedTask(plugin, () -> {
                    tnt.setCustomName(ChatColor.YELLOW + "2");
                }, 20);
                Smash.sced.scheduleSyncDelayedTask(plugin, () -> {
                    tnt.setCustomName(ChatColor.RED + "1");
                }, 40);
                Smash.sced.scheduleSyncDelayedTask(plugin, () -> {
                    tnt.setCustomName("BOOM");
                    TNTPrimed tnt1 = p.getWorld().spawn(e.getBlock().getLocation().add(1.2,0,0), TNTPrimed.class);
                    tnt1.setFuseTicks(0);
                    TNTPrimed tnt2 = p.getWorld().spawn(e.getBlock().getLocation().add(-1.2,0,0), TNTPrimed.class);
                    tnt2.setFuseTicks(0);
                    TNTPrimed tnt3 = p.getWorld().spawn(e.getBlock().getLocation().add(0,0,1.2), TNTPrimed.class);
                    tnt3.setFuseTicks(0);
                    TNTPrimed tnt4 = p.getWorld().spawn(e.getBlock().getLocation().add(0,0,-1.2), TNTPrimed.class);
                    tnt4.setFuseTicks(0);
                    tnt.remove();
                }, 60);

            }
        }else if(Arena.arenaOfPlayer(p,false) != null){
            e.setCancelled(true);
        }


    }

}

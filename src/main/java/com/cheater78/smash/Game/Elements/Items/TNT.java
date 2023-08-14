package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Systems.PlayerManager;
import com.cheater78.smash.Smash;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class TNT extends Item {
    public TNT() {
        super(ChatColor.RED + "TNT", Material.TNT);
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if(!PlayerManager.isInGame(p)) return;
        if(!p.getInventory().getItemInMainHand().isSimilar(get(false))) return;

        p.getInventory().setItemInMainHand(null);
        e.setCancelled(true);

        ArmorStand tnt = (ArmorStand) p.getWorld().spawnEntity(e.getBlock().getLocation().add(0.5,-1.2,0.5), EntityType.ARMOR_STAND);
        tnt.setInvisible(true);
        tnt.setGravity(false);
        tnt.setHelmet(new ItemStack(Material.TNT));
        tnt.setCustomNameVisible(true);
        tnt.setCustomName(ChatColor.GREEN + "3");

        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            tnt.setCustomName(ChatColor.YELLOW + "2");
        }, 20);
        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            tnt.setCustomName(ChatColor.RED + "1");
        }, 40);
        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
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
}

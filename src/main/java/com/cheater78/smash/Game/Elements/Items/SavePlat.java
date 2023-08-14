package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Systems.PlayerManager;
import com.cheater78.smash.Smash;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class SavePlat extends Item {
    public SavePlat() {
        super(ChatColor.WHITE + "SavingCloud", Material.WHITE_DYE);
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!PlayerManager.isInGame(p)) return;
        if (!p.getInventory().getItemInMainHand().isSimilar(this.get(false))) return;

        ArmorStand as = p.getWorld().spawn(new Location(p.getWorld(), ((int)p.getLocation().getX())+0.5, (int)p.getLocation().getY()-2, ((int)p.getLocation().getZ())+0.5), ArmorStand.class);
        as.setGravity(false); as.setInvisible(true); as.setCustomNameVisible(true); as.setCustomName(ChatColor.WHITE + "SaveCloud");
        if(as.getWorld().getBlockAt(as.getLocation().add(0,0,0)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(0,0,0)).setType(Material.SNOW_BLOCK);
        if(as.getWorld().getBlockAt(as.getLocation().add(1,0,0)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(1,0,0)).setType(Material.SNOW_BLOCK);
        if(as.getWorld().getBlockAt(as.getLocation().add(0,0,1)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(0,0,1)).setType(Material.SNOW_BLOCK);
        if(as.getWorld().getBlockAt(as.getLocation().add(-1,0,0)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(-1,0,0)).setType(Material.SNOW_BLOCK);
        if(as.getWorld().getBlockAt(as.getLocation().add(0,0,-1)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(0,0,-1)).setType(Material.SNOW_BLOCK);

        p.getInventory().setItemInMainHand(null);
        e.setCancelled(true);

        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            as.setCustomName(ChatColor.RED + "SaveCloud");
            as.setCustomNameVisible(true);
        }, 20*6);

        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            if(as.getWorld().getBlockAt(as.getLocation().add(0,0,0)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(0,0,0)).setType(Material.AIR);
            if(as.getWorld().getBlockAt(as.getLocation().add(1,0,0)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(1,0,0)).setType(Material.AIR);
            if(as.getWorld().getBlockAt(as.getLocation().add(0,0,1)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(0,0,1)).setType(Material.AIR);
            if(as.getWorld().getBlockAt(as.getLocation().add(-1,0,0)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(-1,0,0)).setType(Material.AIR);
            if(as.getWorld().getBlockAt(as.getLocation().add(0,0,-1)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(0,0,-1)).setType(Material.AIR);
            as.remove();

            p.playSound(as.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.5f, 0);

        }, 20*10);
    }

}

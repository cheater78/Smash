package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Game.Systems.PlayerManager;
import com.cheater78.smash.Smash;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class SpawnEgg extends Item {

    private final double spEggThrowSpeed = 1.2;

    public SpawnEgg() {
        super(ChatColor.DARK_PURPLE + "MobSpawnEGG",    Material.CREEPER_SPAWN_EGG);
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!PlayerManager.isInGame(p)) return;
        if (!p.getInventory().getItemInMainHand().isSimilar(this.get(false))) return;
        SmashGame game = PlayerManager.getGame(p);

        org.bukkit.entity.Item se = p.getWorld().dropItemNaturally(p.getEyeLocation(), new ItemStack(Material.CREEPER_SPAWN_EGG));
        se.setCustomName(ChatColor.DARK_PURPLE + "MobSpawnEGG");
        se.setCustomNameVisible(true);
        se.setVelocity(p.getEyeLocation().getDirection().multiply(spEggThrowSpeed));
        p.getInventory().setItemInMainHand(null);
        e.setCancelled(true);

        ArrayList<EntityType> mobs = new ArrayList<EntityType>() {{
            add(EntityType.ZOMBIE);
            add(EntityType.SKELETON);
            add(EntityType.CREEPER);
            add(EntityType.SPIDER);
            add(EntityType.GHAST);
            add(EntityType.GIANT);
            add(EntityType.CAVE_SPIDER);
            add(EntityType.BLAZE);
            add(EntityType.PIGLIN_BRUTE);
            add(EntityType.VEX);
        }};
        new BukkitRunnable() {
            public void run() {
                if(se.isOnGround()){
                    Entity mob = p.getWorld().spawnEntity(se.getLocation(), mobs.get( (new Random()).nextInt(mobs.size()) ));
                    se.remove();
                    Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, mob::remove, game.getArena().getMobDespawnDelay());
                    cancel();
                }
            }
        }.runTaskTimer(Smash.plugin, 1, 1);
    }

}

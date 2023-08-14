package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Systems.PlayerManager;
import com.cheater78.smash.Smash;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class GravityGrenade extends Item {

    private final double speed = 1.2;
    private final double gravity = 0.01666;

    public GravityGrenade() {
        super(ChatColor.GRAY + "GravityGrenade", Material.FIREWORK_STAR);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!PlayerManager.isInGame(p)) return;
        if (!p.getInventory().getItemInMainHand().isSimilar(this.get(false))) return;

        org.bukkit.entity.Item gg = p.getWorld().dropItemNaturally(p.getEyeLocation(), new ItemStack(Material.FIREWORK_STAR));
        gg.setCustomName(ChatColor.BLACK + "GravityGrenade");
        gg.setCustomNameVisible(true);
        gg.setVelocity(p.getEyeLocation().getDirection().multiply(speed));
        p.getInventory().setItemInMainHand(null);
        e.setCancelled(true);


        new BukkitRunnable() {
            public void run() {
                if(gg.isOnGround()){
                    ArmorStand as = (ArmorStand) gg.getWorld().spawnEntity( gg.getLocation().add(0,-1.5,0), EntityType.ARMOR_STAND);
                    as.setCustomName(ChatColor.GREEN + "GravityGrenade(5)"); as.setCustomNameVisible(true);
                    as.setGravity(false); as.setInvisible(true); as.getEquipment().setHelmet(new ItemStack(Material.BEDROCK));
                    as.setInvulnerable(true);
                    gg.remove();
                    final int[] ticks = {0};
                    new BukkitRunnable() {
                        public void run() {
                            List<Entity> eL = as.getNearbyEntities(3,3,3);
                            for(Entity ent:eL){
                                Vector vec = ent.getLocation().toVector().subtract(as.getEyeLocation().add(0,0.25,0).toVector());
                                ent.setVelocity(new Vector(vec.getX()*((double) -ticks[0] *gravity),ent.getVelocity().add(new Vector(0,0.1/ticks[0],0)).getY(),vec.getZ()*((double) -ticks[0] *gravity)));
                            }
                            if(ticks[0] >= 100){
                                as.setCustomName(ChatColor.RED + "Over");
                                TNTPrimed tnt = (TNTPrimed) as.getWorld().spawnEntity(as.getEyeLocation(), EntityType.PRIMED_TNT);
                                tnt.setFuseTicks(0);
                                as.remove();
                                cancel();
                            }else if(ticks[0] >= 80){
                                as.setCustomName(ChatColor.RED + "GravityGrenade(1)");
                            }else if(ticks[0] >= 60){
                                as.setCustomName(ChatColor.YELLOW + "GravityGrenade(2)");
                            }else if(ticks[0] >= 40){
                                as.setCustomName(ChatColor.YELLOW + "GravityGrenade(3)");
                            }else if(ticks[0] >= 20){
                                as.setCustomName(ChatColor.GREEN + "GravityGrenade(4)");
                            }
                            ticks[0]++;
                        }
                    }.runTaskTimer(Smash.plugin, 1, 1);
                    cancel();
                }
            }
        }.runTaskTimer(Smash.plugin, 1, 1);
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if(!PlayerManager.isInGame(p)) return;

        ItemStack is = e.getItem().getItemStack();

        if(is.getType() == Material.FIREWORK_STAR && !is.isSimilar(get(false)))
            e.setCancelled(true);

    }

}

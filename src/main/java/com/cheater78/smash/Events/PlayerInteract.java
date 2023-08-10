package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Items.Items;
import com.cheater78.smash.Smash;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerInteract implements Listener {

    public static Plugin plugin;

    public static ArrayList<Material> signTypes= new ArrayList<Material>() {{
        add(Material.ACACIA_SIGN); add(Material.ACACIA_WALL_SIGN); add(Material.SPRUCE_SIGN); add(Material.SPRUCE_WALL_SIGN);  add(Material.BIRCH_SIGN); add(Material.BIRCH_WALL_SIGN); add(Material.CRIMSON_SIGN); add(Material.CRIMSON_WALL_SIGN); add(Material.DARK_OAK_SIGN);  add(Material.DARK_OAK_WALL_SIGN); add(Material.JUNGLE_SIGN);  add(Material.JUNGLE_WALL_SIGN); add(Material.OAK_SIGN);  add(Material.OAK_WALL_SIGN); add(Material.WARPED_SIGN);  add(Material.WARPED_WALL_SIGN);
    }};

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(Arena.arenaOfPlayer(p, true) != null) {
            Arena a = Arena.arenaOfPlayer(p, true);

            if(p.getInventory().getItemInMainHand() != null){
                ItemStack i = p.getInventory().getItemInMainHand();

                if(i.isSimilar(Items.rocketLauncher())){
                    p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 1);
                    Fireball fire = p.getWorld().spawn(p.getEyeLocation(), Fireball.class);
                    fire.setShooter(p);
                    fire.setYield(2.5f);
                    fire.setVelocity(fire.getVelocity().multiply(3));
                    p.getInventory().setItemInMainHand(null);
                    e.setCancelled(true);
                }
                if(i.isSimilar(Items.flameFlower())){
                    p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5f, 1);
                    SmallFireball fire = p.getWorld().spawn(p.getEyeLocation(), SmallFireball.class);
                    fire.setShooter(p);
                    fire.setVelocity(fire.getVelocity().multiply(3));
                    if(i.getAmount() == 1){
                        p.getInventory().setItemInMainHand(null);
                    }else{
                        i.setAmount(i.getAmount()-1);
                        p.getInventory().setItemInMainHand(i);;
                    }
                    e.setCancelled(true);
                }
                if(i.isSimilar(Items.gravityGrenade())){
                    Item gg = p.getWorld().dropItemNaturally(p.getEyeLocation(), new ItemStack(Material.FIREWORK_STAR));
                    gg.setCustomName(ChatColor.BLACK + "GravityGrenade");
                    gg.setCustomNameVisible(true);
                    gg.setVelocity(p.getEyeLocation().getDirection().multiply(1.2));
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
                                            ent.setVelocity(new Vector(vec.getX()*(-ticks[0]/60),ent.getVelocity().getY(),vec.getZ()*(-ticks[0]/60)));
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
                                }.runTaskTimer(plugin, 1, 1);
                                cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 1, 1);
                }

                if(i.isSimilar(Items.healShroom())){
                    if(p.getLevel() <= 50){
                        p.setLevel(0);
                    }else{
                        p.setLevel(p.getLevel()-50);
                    }
                    p.playSound(p.getEyeLocation(), Sound.ENTITY_PLAYER_BURP, 0.5f, 1);
                    p.getInventory().setItemInMainHand(null);
                    e.setCancelled(true);
                }

                if(i.isSimilar(Items.savePlat())){
                    ArmorStand as = p.getWorld().spawn(new Location(p.getWorld(), ((int)p.getLocation().getX())+0.5, (int)p.getLocation().getY()-2, ((int)p.getLocation().getZ())+0.5), ArmorStand.class);
                    as.setGravity(false); as.setInvisible(true); as.setCustomNameVisible(true); as.setCustomName(ChatColor.WHITE + "SaveCloud");
                    if(as.getWorld().getBlockAt(as.getLocation().add(0,0,0)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(0,0,0)).setType(Material.SNOW_BLOCK);
                    if(as.getWorld().getBlockAt(as.getLocation().add(1,0,0)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(1,0,0)).setType(Material.SNOW_BLOCK);
                    if(as.getWorld().getBlockAt(as.getLocation().add(0,0,1)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(0,0,1)).setType(Material.SNOW_BLOCK);
                    if(as.getWorld().getBlockAt(as.getLocation().add(-1,0,0)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(-1,0,0)).setType(Material.SNOW_BLOCK);
                    if(as.getWorld().getBlockAt(as.getLocation().add(0,0,-1)).getBlockData().getMaterial() == Material.AIR) as.getWorld().getBlockAt(as.getLocation().add(0,0,-1)).setType(Material.SNOW_BLOCK);

                    p.getInventory().setItemInMainHand(null);
                    e.setCancelled(true);

                    Smash.sced.scheduleSyncDelayedTask(plugin, () -> {
                        as.setCustomName(ChatColor.RED + "SaveCloud");
                        as.setCustomNameVisible(true);
                    }, 20*6);

                    Smash.sced.scheduleSyncDelayedTask(plugin, () -> {
                        if(as.getWorld().getBlockAt(as.getLocation().add(0,0,0)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(0,0,0)).setType(Material.AIR);
                        if(as.getWorld().getBlockAt(as.getLocation().add(1,0,0)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(1,0,0)).setType(Material.AIR);
                        if(as.getWorld().getBlockAt(as.getLocation().add(0,0,1)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(0,0,1)).setType(Material.AIR);
                        if(as.getWorld().getBlockAt(as.getLocation().add(-1,0,0)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(-1,0,0)).setType(Material.AIR);
                        if(as.getWorld().getBlockAt(as.getLocation().add(0,0,-1)).getBlockData().getMaterial() == Material.SNOW_BLOCK) as.getWorld().getBlockAt(as.getLocation().add(0,0,-1)).setType(Material.AIR);
                        as.remove();
                    }, 20*10);
                }

                if(i.isSimilar(Items.sugarRush())){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20*5, 5,false,false,true));
                    p.getInventory().setItemInMainHand(null);
                    e.setCancelled(true);
                }

                if(i.isSimilar(Items.opGapple())){
                    Firework firework = p.getWorld().spawn(p.getEyeLocation(), Firework.class);
                    FireworkMeta fmeta = firework.getFireworkMeta();
                    FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.RED).build();
                    fmeta.addEffect(effect);
                    firework.setFireworkMeta(fmeta);
                    firework.detonate();
                    if(p.getLevel() <= (int)(a.maxExp/5)){
                        p.setLevel(0);
                    }else {
                        p.setLevel(p.getLevel()-(int)(a.maxExp/5));
                    }
                    p.setInvulnerable(true);
                    p.setGlowing(true);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*7, 1,false,false,true));
                    p.sendTitle(" ", ChatColor.GOLD + "<Invulnerable>", 5, 20*6,15);

                    p.getInventory().setItemInMainHand(null);
                    e.setCancelled(true);
                    Smash.sced.scheduleSyncDelayedTask(plugin, () -> {
                        p.setInvulnerable(false);
                        p.setGlowing(false);
                        p.resetTitle();
                    }, 20*7);
                }

                if(i.isSimilar(Items.spawnEgg())){
                    Item se = p.getWorld().dropItemNaturally(p.getEyeLocation(), new ItemStack(Material.CREEPER_SPAWN_EGG));
                    se.setCustomName(ChatColor.DARK_PURPLE + "MobSpawnEGG");
                    se.setCustomNameVisible(true);
                    se.setVelocity(p.getEyeLocation().getDirection().multiply(1.2));
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
                    }};
                    new BukkitRunnable() {
                        public void run() {
                            if(se.isOnGround()){
                                Entity mob = p.getWorld().spawnEntity(se.getLocation(), mobs.get( (new Random()).nextInt(mobs.size()) ));
                                se.remove();
                                Smash.sced.scheduleSyncDelayedTask(plugin, () -> {
                                    mob.remove();
                                }, 20*Smash.arenaMobDespawnTime);
                                cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 1, 1);
                }

                if(i.isSimilar(Items.flappyRocket())){
                    p.setVelocity(p.getEyeLocation().getDirection().multiply(1).add(new Vector(0,1.5f,0)));

                    if(i.getAmount() == 1){
                        p.getInventory().setItemInMainHand(null);
                    }else{
                        i.setAmount(i.getAmount()-1);
                        p.getInventory().setItemInMainHand(i);;
                    }
                    e.setCancelled(true);
                }

            }
        }else if(Arena.arenaOfPlayer(p, false) != null) {
            Arena a = Arena.arenaOfPlayer(p, false);
            if (p.getInventory().getItemInMainHand() != null) {
                ItemStack i = p.getInventory().getItemInMainHand();
                if(Smash.useLobbyItems && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
                    if(i.getType() == Material.NETHER_STAR){
                        p.performCommand("smash start");
                    }
                    if(i.getType() == Material.CHEST){
                        p.performCommand("smash settings");
                    }
                    if(i.getType() == Material.BARRIER){
                        p.performCommand("smash leave");
                    }
                }
            }
        }
        if(e.getClickedBlock() != null){
            if(signTypes.contains(e.getClickedBlock().getType()) && p.hasPermission("smash.signs.use")){
                if(signChange.getCommandFromSign(e.getClickedBlock().getLocation()) != null && Smash.useSigns){
                    p.performCommand(signChange.getCommandFromSign(e.getClickedBlock().getLocation()));
                }else if((((Sign) e.getClickedBlock().getState()).getLine(0).contains("smash")
                        || ((Sign) e.getClickedBlock().getState()).getLine(0).contains("Smash"))
                        && ((Sign) e.getClickedBlock().getState()).getLine(0).contains("[")
                        && ((Sign) e.getClickedBlock().getState()).getLine(0).contains("]")){
                    if(Smash.useSigns){
                        p.sendMessage(ChatColor.RED + "If u tried to use a Smash related sign, replace the Sign. This one is not registered!");
                    }else {
                        p.sendMessage(ChatColor.RED + "If u tried to use a Smash related sign, Signs are turned off in the Config!");
                    }
                }
            }
        }


    }


}

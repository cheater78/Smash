package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Commands.smash;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Move implements Listener {

    public static Plugin plugin;

    public static ArrayList<Player> onDJumpCoolDown = new ArrayList<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if(Arena.arenaOfPlayer(p,true) != null){
            Arena a = Arena.arenaOfPlayer(p, true);

            if(!smash.isInRect(p.getLocation(),a.minLoc,a.maxLoc)){
                if(p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE){
                    Arena.kill(p);
                }else {
                    p.teleport(a.arenaSpecSpawn);
                }

            }

            if((p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) && p.isFlying()){
                p.setExp(0);
                p.setVelocity(p.getEyeLocation().getDirection().multiply(0.8).setY(1));
                e.setCancelled(true);
                p.setFlying(false);
                p.setAllowFlight(false);
            }

            if(!p.getAllowFlight() && !onDJumpCoolDown.contains(p)){
                onDJumpCoolDown.add(p);
                new BukkitRunnable() {
                    public void run() {
                        if(p.getExp() <= 0.9){
                            p.setExp(p.getExp()+0.05f);
                        }else{
                            p.setAllowFlight(true);
                            p.setExp(0.98f);
                            onDJumpCoolDown.remove(p);
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 5, 5);
            }




        }
    }

}

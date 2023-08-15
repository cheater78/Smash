package com.cheater78.smash.Game.Systems;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class DoubleJumpHandler implements Listener {


    @EventHandler
    public void onFlightAttemp(PlayerToggleFlightEvent e){
        Player p = e.getPlayer();

        if(Arena.arenaOfPlayer(p,true) != null){
            Arena a = Arena.arenaOfPlayer(p,true);

            if(p.getGameMode() != GameMode.CREATIVE && !p.isFlying() && p.getExp() >= 0.95f){
                /*
                p.setExp(0);
                p.setVelocity(p.getEyeLocation().getDirection().multiply(0.8).setY(1));
                e.setCancelled(true);
                p.setFlying(false);
                p.setAllowFlight(false);*/
                //Handling in PlayerMoveEvent
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if(Arena.arenaOfPlayer(p,true) != null){
            Arena a = Arena.arenaOfPlayer(p, true);

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

    public static void start(ArrayList<Player> players){
        for(Player p:players){
            p.setAllowFlight(true);
            p.setExp(0.99f);
        }
    }

    public static void stop(ArrayList<Player> players){
        for(Player p:players){
            p.setAllowFlight(false);
            p.setExp(0);
        }
    }

    public static void onLeaveFlight(Player p){
        p.setAllowFlight(false);
        p.setExp(0);
    }

}

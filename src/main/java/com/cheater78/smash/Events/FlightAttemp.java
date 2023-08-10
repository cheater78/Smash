package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class FlightAttemp implements Listener {

    public static Plugin plugin;

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

package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Hunger implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(Arena.arenaOfPlayer(p,false) != null){
                e.setCancelled(true);
            }
            if(Arena.arenaOfPlayer(p,true) != null){
                e.setCancelled(true);
            }
        }
    }

}

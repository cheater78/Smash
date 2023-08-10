package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class Regen implements Listener {

    @EventHandler
    public void onRegen(EntityRegainHealthEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(Arena.arenaOfPlayer(p,true) != null){
                e.setCancelled(true);
            }
        }
    }

}

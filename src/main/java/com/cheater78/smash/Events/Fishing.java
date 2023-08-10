package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class Fishing implements Listener {

    @EventHandler
    public void onFishing(PlayerFishEvent e){
        Player p = e.getPlayer();
        if(Arena.arenaOfPlayer(p,true) != null){
            Arena a = Arena.arenaOfPlayer(p,true);

            if(e.getState() == PlayerFishEvent.State.REEL_IN){
                p.setVelocity(e.getHook().getLocation().toVector().subtract(p.getLocation().toVector()).multiply(new Vector(0.8,0.1,0.8)).add(new Vector(0,0.8,0)));
                p.getInventory().setItemInMainHand(null);
            }
        }
    }

}

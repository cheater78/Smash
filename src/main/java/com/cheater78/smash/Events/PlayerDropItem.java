package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        Item i = e.getItemDrop();

        if(Arena.arenaOfPlayer(p,true) != null){
            if(p.getInventory().getItem(4) != null){
                p.getInventory().setItem(4,null);
            }
            i.remove();
        }else if(Arena.arenaOfPlayer(p,false) != null){
            e.setCancelled(true);
        }



    }

}

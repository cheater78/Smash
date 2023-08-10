package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Items.Items;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerPickUp implements Listener {

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            Item i = e.getItem();
            ItemStack is = i.getItemStack();

            if(Arena.arenaOfPlayer(p,true) != null){
                Arena a = Arena.arenaOfPlayer(p,true);

                if(is.getType() == Material.FIREWORK_STAR && !is.isSimilar(Items.gravityGrenade())){
                    e.setCancelled(true);
                }

                if(p.getInventory().getItem(4) == null && Items.isValid(is)){
                    if(is.getType() == Material.POPPY) is.setAmount(5);
                    else if(is.getType() == Material.FIREWORK_ROCKET) is.setAmount(5);
                    else is.setAmount(1);
                    p.getInventory().setItem(4,is);
                    i.remove();
                }
                e.setCancelled(true);

            }

        }
    }
}

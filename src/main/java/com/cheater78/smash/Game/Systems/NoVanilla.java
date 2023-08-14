package com.cheater78.smash.Game.Systems;

import com.cheater78.smash.Game.SmashGame;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class NoVanilla implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if(!PlayerManager.isInGame(p)) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if(p.getInventory().getItem(p.getInventory().getHeldItemSlot()) == null) return;
        if(!PlayerManager.isInGame(p)) return;
        SmashGame game = PlayerManager.getGame(p);
        Item i = e.getItemDrop();
        if(game.isGameActive()){
            p.getInventory().getItem(p.getInventory().getHeldItemSlot()).setType(Material.AIR);
            i.remove();
        }else e.setCancelled(true);
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if(!PlayerManager.isInGame(p)) return;

        e.setCancelled(true);
    }

}

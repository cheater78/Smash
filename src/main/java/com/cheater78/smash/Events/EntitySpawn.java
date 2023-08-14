package com.cheater78.smash.Events;

import com.cheater78.smash.Commands.smash;
import com.cheater78.smash.Game.Elements.Items;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Serialize.Serializable.Location;
import com.cheater78.smash.Smash;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class EntitySpawn implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        if(!(e.getEntity() instanceof Item)) return;
        for (SmashGame game : Smash.games.values()){
            if(!game.isGameActive()) continue;
            if(! new Location(e.getEntity().getLocation()).isInRegion(game.getArena().getPos1(), game.getArena().getPos2())) continue;
            if(Items.isSmashItem(((Item)e.getEntity()).getItemStack()))
                continue;
            else e.getEntity().remove();
        }
    }
}

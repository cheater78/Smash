package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Commands.smash;
import com.cheater78.smash.Items.Items;
import com.cheater78.smash.Smash;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class EntitySpawn implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        if(e.getEntity() instanceof Item){
            Item ientity = (Item) e.getEntity();
            for(Arena a: Smash.arenas){
                if(a.gameActive){
                    if(smash.isInRect(ientity.getLocation(),a.minLoc,a.maxLoc)){
                        ItemStack i = ientity.getItemStack();
                        if(Items.isValid(i)){

                        }else {
                            ientity.remove();
                        }
                    }
                }
            }



        }
    }
}

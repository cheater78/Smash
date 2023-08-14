package com.cheater78.smash.Game.Systems;

import com.cheater78.smash.Config.ItemSpawnConfig;
import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Elements.Items;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Serialize.Serializable.Location;
import com.cheater78.smash.Utils.BukkitWorldLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {
    private Map<Item, Integer> items;
    private boolean itemPoolChanged = true;
    private List<Item> itemPool;

    public ItemManager(){
        items = new HashMap<>();
        itemPool = new ArrayList<>();
        for(Item i : Items.getAll())
            items.put(i, ItemSpawnConfig.getItemOccurence(i));
    }

    public int getItemOccurence(Item item){ return items.get(item); }

    public void setItemSpawnOccurence(Item item, int occ){
        if(item == null) throw new NullPointerException();
        if(occ < 0) throw new IllegalArgumentException();
        if(!items.containsKey(item)) throw new IllegalArgumentException();
        items.put(item,occ);
        itemPoolChanged = true;
    }

    public void spawnItems(SmashGame game){
        if(!game.isGameActive()) return;
        if(itemPoolChanged) updateItemPool();

        for(Location loc : game.getArena().getItemSpawns()){
            BukkitWorldLoader.getWorld(game.getArena().getWorld())
                    .dropItemNaturally(
                            loc.toBukkitLocation(),
                            itemPool.get((int) (Math.random() * itemPool.size())).get(false)
                            );
        }
    }

    public void spawnItem(SmashGame game){
        if(!game.isGameActive()) return;
        if(itemPoolChanged) updateItemPool();
        Location loc = game.getArena().getItemSpawns()
                .get((int) (Math.random() * game.getArena().getItemSpawns().size()));
        BukkitWorldLoader.getWorld(game.getArena().getWorld())
                .dropItemNaturally(
                        loc.toBukkitLocation(),
                        itemPool.get((int) (Math.random() * itemPool.size())).get(false)
                );

    }


    private void updateItemPool(){
        itemPool.clear();
        for(Item item : items.keySet()){
            for(int i = 0; i < items.get(item); i++){
                itemPool.add(item);
            }
        }
        itemPoolChanged = false;
    }
}

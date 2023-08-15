package com.cheater78.smash.Game.Systems;

import com.cheater78.smash.Config.ItemSpawnConfig;
import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Serialize.Serializable.Location;
import com.cheater78.smash.Smash;
import com.cheater78.smash.Utils.BukkitWorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager implements Listener {

    private Map<Item, Integer> items;

    private List<Item> getItemPool(){
        List<Item> itemPool = new ArrayList<>();
        for(Item item : items.keySet()){
            for(int i = 0; i < items.get(item); i++){
                itemPool.add(item);
            }
        }
        return itemPool;
    }

    public ItemManager(){
        items = new HashMap<>();
        for(Item i : Items.getAll())
            items.put(i, ItemSpawnConfig.getItemOccurence(i));

        Bukkit.getServer().getPluginManager().registerEvents(this, Smash.plugin);
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

    public static boolean isSmashItem(ItemStack iStack){
        for(ItemStack is : getAll(false)){
            if(iStack.getType() == is.getType() && iStack.getItemMeta().getLore().equals(is.getItemMeta().getLore())){
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            org.bukkit.entity.Item i = e.getItem();
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

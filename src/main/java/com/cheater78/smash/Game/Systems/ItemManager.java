package com.cheater78.smash.Game.Systems;

import com.cheater78.smash.Config.GameConfig;
import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Elements.Items.*;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Serialize.Serializable.Location;
import com.cheater78.smash.Serialize.Serializer;
import com.cheater78.smash.Smash;
import com.cheater78.smash.Utils.BukkitWorldLoader;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ItemManager implements Listener {

    SmashGame game;
    private final Map<Item, Integer> items;
    private final Serializer<Item> serializer;
    private BukkitTask spawnHandle;

    public static List<Item> all = Lists.newArrayList(
            new RocketLauncher(),
            new FlameThrower(),
            new FlappyRocket(),
            new GodApple(),
            new GrapplingHook(),
            new GravityGrenade(),
            new HealShroom(),
            new SavePlat(),
            new Smasher(),
            new SpawnEgg(),
            new SugarRush(),
            new TNT()
    );

    public ItemManager(SmashGame game){
        this.items = new HashMap<>();
        this.game = game;
        this.serializer = new Serializer<>("Arenas/" + game.getArena().getName() + "/items.json");
        setupItems();

        Bukkit.getServer().getPluginManager().registerEvents(this, Smash.plugin);
    }

    public void start(){
        spawnHandle = new BukkitRunnable() {
            @Override
            public void run() {
                spawnItems(game);
            }
        }.runTaskTimer(Smash.plugin, 20, game.getArena().getItemSpawnDelay());
    }

    public void stop(){
        spawnHandle.cancel();
    }

    private void setupItems(){
        List<Item> missing = all;
        List<Item> fromFile = serializer.loadAll();

        for(Item item : fromFile){
            items.put(item, item.getOccurence());
            missing.remove(item);
        }
        for(Item item : missing){
            items.put(item, 0);
            item.setOccurence(0);
            serializer.save(item);
        }
        for(Item item : items.keySet())
            Bukkit.getServer().getPluginManager().registerEvents(item, Smash.plugin);
    }

    public int getItemOccurence(Item item){ return items.get(item); }
    public void changeItem(Item item, int occurence){
        if(item == null) throw new NullPointerException();
        if(occurence < 0) throw new IllegalArgumentException();
        items.put(item, occurence);
        item.setOccurence(occurence);
        serializer.save(item);
    }

    private List<Item> getItemPool(){
        List<Item> itemPool = new ArrayList<>();
        for(Item item : items.keySet()){
            for(int i = 0; i < items.get(item); i++){
                itemPool.add(item);
            }
        }
        return itemPool;
    }


    public void spawnItems(SmashGame game){
        if(!game.isGameActive()) return;

        for(Location loc : game.getArena().getItemSpawns()){
            BukkitWorldLoader.getWorld(game.getArena().getWorld())
                    .dropItemNaturally(
                            loc.toBukkitLocation(),
                            getItemPool().get((int) (Math.random() * getItemPool().size())).get(false)
                            );
        }
    }

    public static Item getSmashItem(ItemStack iStack){
        for(Item i : all){
            if(iStack.getType() == i.get(false).getType() && iStack.getItemMeta().getLore().equals(i.get(false).getItemMeta().getLore())){
                return i;
            }
        }
        return null;
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if(!PlayerManager.isInGame(p)) return;
        ItemStack is = e.getItem().getItemStack();
        SmashGame game = PlayerManager.getGame(p);

        if(     (is.getType() == Material.FIREWORK_STAR && !is.isSimilar(new GravityGrenade().get(false)))
             || (is.getType() == Material.CREEPER_SPAWN_EGG && !is.isSimilar(new SpawnEgg().get(false)))    ){
            e.setCancelled(true);
            return;
        }

        Item item = getSmashItem(is);
        if(p.getInventory().getItem(GameConfig.itemHotbarSlot) == null && item != null){
            is.setAmount(item.getPickupAmount());
            p.getInventory().setItem(GameConfig.itemHotbarSlot, is);
            e.getItem().remove();
        } else if (item == null) {
            e.getItem().remove();
        }
        e.setCancelled(true);
    }

}

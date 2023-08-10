package com.cheater78.smash.Serialize.Serializable;

import com.cheater78.smash.Commands.smash;
import com.cheater78.smash.Config.PluginConfig;
import com.cheater78.smash.Utils.BukkitWorldLoader;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;

import java.util.ArrayList;
import java.util.UUID;

public class Arena implements Serializable {

    UUID uuid;

    private final String name;
    private final String world;
    private Location pos1;
    private Location pos2;

    private Location lobbySpawn;
    private Location specSpawn;
    private Location evacuationPoint;
    private ArrayList<Location> playerSpawns;
    private ArrayList<Location> itemSpawns;

    private int itemSpawnDelay = 30;
    private int mobDespawnDelay = 20;

    /*
    Arena is an unsafe data object, validate all data before construction / set
     */
    public Arena(String name, String world){
        if(name == null) throw new NullPointerException();
        if(world == null) throw new NullPointerException();
        this.name = name;
        this.world = world;
        this.uuid = new UUID(name.hashCode(), name.hashCode());
        this.evacuationPoint = PluginConfig.defaultEvacPoint;
    }

    @Override
    public UUID getUuid() { return uuid; }

    public String getName() { return name; }
    public String getWorld() { return world; }
    public Location getPos1() { return pos1; }
    public Location getPos2() { return pos2; }
    public Location getLobbySpawn() { return lobbySpawn; }
    public Location getSpecSpawn() { return specSpawn; }
    public Location getEvacuationPoint() { return evacuationPoint; }
    public ArrayList<Location> getItemSpawns() { return itemSpawns; }
    public ArrayList<Location> getPlayerSpawns() { return playerSpawns; }
    public int getItemSpawnDelay() { return itemSpawnDelay; }
    public int getMobDespawnDelay() { return mobDespawnDelay; }

    public void setPos1(Location pos1) { this.pos1 = pos1; }
    public void setPos2(Location pos2) { this.pos2 = pos2; }
    public void setLobbySpawn(Location lobbySpawn) { this.lobbySpawn = lobbySpawn; }
    public void setSpecSpawn(Location specSpawn) { this.specSpawn = specSpawn; }
    public void setEvacuationPoint(Location evacuationPoint) { this.evacuationPoint = evacuationPoint; }
    public void setItemSpawns(ArrayList<Location> itemSpawns) { this.itemSpawns = itemSpawns; }
    public void setPlayerSpawns(ArrayList<Location> playerSpawns) { this.playerSpawns = playerSpawns; }
    public boolean addItemSpawn(Location itemSpawn){ return this.itemSpawns.add(itemSpawn); }
    public boolean addPlayerSpawn(Location playerSpawn){ return this.playerSpawns.add(playerSpawn); }
    public void setItemSpawnDelay(int itemSpawnDelay) { this.itemSpawnDelay = itemSpawnDelay; }
    public void setMobDespawnDelay(int mobDespawnDelay) { this.mobDespawnDelay = mobDespawnDelay; }

    public boolean isValid(){
        return pos1 != null
                & pos2 != null
                & lobbySpawn != null
                & specSpawn != null
                & playerSpawns != null
                & itemSpawns != null;
    }

    public void clearEntities(){
        for (Entity e: BukkitWorldLoader.getWorld(world).getEntities() ){
            if(e instanceof Animals || e instanceof Monster || e instanceof Item){
                if(new Location(e.getLocation()).isInRegion(pos1, pos2)){
                    e.remove();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Arena{" +
                "name='" + name + '\'' +
                ", world='" + world + '\'' +
                ", pos1=" + pos1 +
                ", pos2=" + pos2 +
                ", lobbySpawn=" + lobbySpawn +
                ", specSpawn=" + specSpawn +
                ", playerSpawns=" + playerSpawns +
                ", itemSpawns=" + itemSpawns +
                '}';
    }
}

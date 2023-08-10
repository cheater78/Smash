package com.cheater78.smash.Game;

import com.cheater78.smash.API.RecoverItems;
import com.cheater78.smash.Commands.smash;
import com.cheater78.smash.Config.PluginConfig;
import com.cheater78.smash.Game.Elements.GUI.InventoryGUI;
import com.cheater78.smash.Game.Elements.ItemManager;
import com.cheater78.smash.Serialize.Serializable.Arena;
import com.cheater78.smash.Serialize.Serializable.Location;
import com.cheater78.smash.Serialize.Serializer;
import com.cheater78.smash.Smash;
import com.cheater78.smash.Utils.BukkitWorldLoader;
import com.cheater78.smash.World.RestoreWorld;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;

public class SmashGame {

    //ARENA (ONE TIME SETUP, GET SAVED TO FILE)
    private Arena arena;

    //ITEM-MANAGER
    private ItemManager itemManager;

    //MATCH STATES
    private boolean ready = false;
    private boolean gameActive = false;

    private boolean playerCountChanged;
    private ArrayList<Player> arenaPlayers;
    private ArrayList<Player> specPlayers;

    //MATCH SETTINGS
    private double lives = PluginConfig.defaultLives;
    private int maxExp = PluginConfig.defaultMaxDamage;

    //Serialize
    Serializer<Arena> serializer = new Serializer<>("Arenas.json");

    public SmashGame(String name, String worldName, boolean fromFile){
        if(name == null    || worldName == null) throw new NullPointerException();
        if(name.equals("") || worldName.equals("") ) throw new IllegalArgumentException();

        this.arenaPlayers = new ArrayList<>();
        this.specPlayers = new ArrayList<>();
        this.playerCountChanged = true;

        if(!fromFile){
            // new SmashGame / Arena
            this.arena = new Arena(name, worldName);

        } else {
            this.arena = serializer.load(new UUID(name.hashCode(), name.hashCode()));
        }



        this.itemManager = new ItemManager();
        this.ready = arena.isValid();
    }

    public Arena getArena(){ return arena; }
    public ItemManager getItemManager() { return itemManager; }
    public double getLives() { return lives; }
    public int getMaxExp() { return maxExp; }
    public boolean isReady(){ return ready; }
    public boolean isReady(Player p){
        p.sendMessage(ChatColor.RED + "Arena is currently rebuilding!");
        return ready;
    }

    public boolean isGameActive(){ return gameActive; }
    public void setReady(boolean ready) { this.ready = ready; }
    public void setGameActive(boolean gameActive) { this.gameActive = gameActive; }

    public List<Player> getPlavers(boolean includeSpectators){
        List<Player> allPlayers = new ArrayList<>(arenaPlayers);
        if(includeSpectators)
            allPlayers.addAll(specPlayers);
        return allPlayers;
    }

    public boolean containsPlayer(Player p, boolean includeSpectators){
        return (includeSpectators) ?
                (arenaPlayers.contains(p) || specPlayers.contains(p))
                : (arenaPlayers.contains(p));
    }

    private Location getConfigPos(String cmd, CommandSender s, String[] args, boolean blockCoords){
        if(args[2].equalsIgnoreCase(cmd) && smash.hasPerm(s,"smash.config."+cmd)){
            if((args.length == 6 && blockCoords) || (args.length == 8 && !blockCoords)){
                try {
                    s.sendMessage(ChatColor.GREEN + "Arena point created successfully");
                    if(blockCoords)
                        return new Location(arena.getWorld(), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                    else
                        return new Location(arena.getWorld(), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Float.parseFloat(args[6]), Float.parseFloat(args[7]));
                }catch (Exception ignore){ s.sendMessage(ChatColor.RED + "Wrong usage!(/smash config [arena] "+cmd+" [x] [y] [z]" + ((!blockCoords) ? " [yaw] [pitch]" : "") + ")"); return null;}
            }
            if(!(s instanceof Player)){
                s.sendMessage(ChatColor.RED + "Wrong usage!(/smash config [arena] "+cmd+" [x] [y] [z]" + ((!blockCoords) ? " [yaw] [pitch]" : "") + ")"); return null;
            }else {
                Player p = (Player) s;
                if (args.length == 3){
                    if(!p.getWorld().getName().equals(arena.getWorld())){  p.sendMessage(ChatColor.DARK_RED + "Arena must be in one world!"); return null; }
                    try {
                        s.sendMessage(ChatColor.GREEN + "Arena point created successfully");
                        return new Location(p.getLocation());
                    }catch (Exception ignore){ s.sendMessage(ChatColor.RED + "Wrong usage!(/smash config [arena] "+cmd+" [x] [y] [z]" + ((!blockCoords) ? " [yaw] [pitch]" : "") + ")"); return null; }
                }else { s.sendMessage(ChatColor.RED + "Wrong usage!(/smash config [arena] "+cmd+" <x> <y> <z>" + ((!blockCoords) ? " <yaw> <pitch>" : "") + ")");  return null; }
            }
        }
        return null;
    }

    // GAME SETUP

    public void onConfig(CommandSender s, String[] args){

        if(args.length < 3){ s.sendMessage(ChatColor.RED + "Wrong usage!(/smash config [arena] [option])"); return; }

        Location loc;
        loc = getConfigPos("pos1", s, args, true);
        if(loc != null)
            arena.setPos1(loc);
        loc = getConfigPos("pos2", s, args, true);
        if(loc != null)
            arena.setPos2(loc);
        loc = getConfigPos("setLobbySpawn", s, args, true);
        if(loc != null)
            arena.setLobbySpawn(loc);
        loc = getConfigPos("setSpecSpawn", s, args, true);
        if(loc != null)
            arena.setSpecSpawn(loc);


        loc = getConfigPos("addPlayerSpawn", s, args, true);
        if(loc != null)
            arena.addPlayerSpawn(loc);
        loc = getConfigPos("addItemSpawn", s, args, true);
        if(loc != null)
            arena.addItemSpawn(loc);


    }

    public boolean onSave(Player p){
        if(isGameActive()) throw new IllegalStateException();

        if(arena.getPos1() == null || arena.getPos2() == null){ p.sendMessage(ChatColor.RED + "Dimensions unset!"); return false; }
        if(arena.getLobbySpawn() == null){ p.sendMessage(ChatColor.RED + "LobbySpawn unset!"); return false; }
        if(arena.getSpecSpawn() == null ){ p.sendMessage(ChatColor.RED + "SpecSpawn unset!"); return false; }
        if(arena.getPlayerSpawns() == null || arena.getPlayerSpawns().size() == 0){ p.sendMessage(ChatColor.RED + "PlayerSpawns missing!"); return false; }
        if(arena.getItemSpawns() == null || arena.getItemSpawns().size() == 0){ p.sendMessage(ChatColor.RED + "ItemSpawns missing!"); return false; }

        serializer.save(arena);

        //TODO: if(!Smash.games.keySet().contains(arena.getName())) Smash.games.put(arena.getName(), this);
        World world = BukkitWorldLoader.getWorld(arena.getWorld());
        BukkitWorldLoader.evacuateWorld(world, arena.getEvacuationPoint());
        BukkitWorldLoader.backupWorld(world, Smash.worlBackupSuffix, true);
        p.sendMessage(ChatColor.RED + "Arena " + arena.getName() + " saved!");
        return true;
    }

    public void onDelete(){
        //TODO: if(!Smash.games.keySet().contains(arena.getName())) Smash.games.remove(arena.getName());
        serializer.remove(arena);
    }

    // GAME EVENTS

    public void onJoin(Player p){
        if(!isReady(p)) return;
        p.sendMessage(ChatColor.GREEN + "joining " + arena.getName() + "...");
        Smash.itemSaver.onJoin(p);
        arenaPlayers.add(p);

        p.teleport(arena.getLobbySpawn().toBukkitLocation());

        setPlayerDefault(p, GameMode.ADVENTURE);

        InventoryGUI.setLobbyItems(p);


    }

    public void onLeave(Player p, Location loc){
        if(arenaPlayers.contains(p))
            arenaPlayers.remove(p);
        else if(specPlayers.contains(p))
            specPlayers.remove(p);
        else {
            p.sendMessage(ChatColor.RED + "You are currently not in a Lobby!");
            return;
        }
        p.sendMessage(ChatColor.GREEN + "Returning to lobby...");
        p.teleport(loc.toBukkitLocation());

        //TODO: removeDoubleJump(p);

        setPlayerDefault(p, GameMode.SURVIVAL);
        Smash.itemSaver.onLeave(p);

        if(getPlavers(false).size() == 0 && isGameActive())
            onStop();
    }

    public void onSpectate(Player p){
        p.sendMessage(ChatColor.GREEN + "Spectating:  " + arena.getName());
        Smash.itemSaver.onJoin(p);
        specPlayers.add(p);

        p.teleport(arena.getLobbySpawn().toBukkitLocation());
        setPlayerDefault(p, GameMode.SPECTATOR);

    }


    public void onStart(Player p){
        if(!isReady(p)) return;
        if(isGameActive()) throw new IllegalStateException();



        for(Player ap: arenaPlayers){
            ap.teleport(arena.getPlayerSpawns().get((int)(Math.random()*arena.getPlayerSpawns().size())).toBukkitLocation());
            setPlayerStart(p);
            //TODO: initDoubleJump(p);
            ap.sendMessage(ChatColor.GREEN + "Game started!");
            ap.sendTitle(ChatColor.GREEN + "Game started!","",5,10,10);
        }

        arena.clearEntities();
        setGameActive(true);
    }

    public void onStop(){
        if(!isGameActive()) throw new IllegalStateException();



        for(Player p : getPlavers(true)){
            p.sendMessage(ChatColor.RED + "Smash Arena stopped!");
            onLeave(p, arena.getEvacuationPoint());
        }
        setGameActive(false);
        onRebuild();
    }

    public void onEnd(){
        if(!isGameActive()) throw new IllegalStateException();



        for(Player p : getPlavers(true))
            teleportToLobby(p, arena.getEvacuationPoint());
        setGameActive(false);
        onRebuild();
    }




    public void onRebuild(){
        if(isGameActive()) throw new IllegalStateException();
        if(getPlavers(true).size() > 0) throw new IllegalStateException();
        setReady(false);

        BukkitWorldLoader.loadBackup(arena.getWorld(), Smash.worlBackupSuffix, false);

        setReady(true);
    }

    private void onDeath(Player p){
        p.getInventory().clear();

        p.playSound(p.getEyeLocation(), Sound.ENTITY_VILLAGER_HURT, 0.5f, 1);
        p.sendTitle(ChatColor.DARK_RED + "YouDied!", ChatColor.GREEN + "<You are currently under 3s spawn protection>",5, 45, 10);

        p.setFireTicks(0);
        p.setExp(0);
        p.setLevel(0);
        p.setInvisible(true);

        if(player1HP(p)){
            // Player -> Game Over
            p.setMaxHealth(20);
            p.setHealthScale(20);
            p.setGameMode(GameMode.SPECTATOR);
            p.teleport(arena.getSpecSpawn().toBukkitLocation());
            p.sendTitle(ChatColor.DARK_RED + "GameOver!", ChatColor.GOLD + "/smash leave" + ChatColor.GREEN + " to return MainLobby",5, 20, 40);
            arenaPlayers.remove(p);
            specPlayers.add(p);

            if(arenaPlayers.size() <= 1){
                // Game -> Game Over
                onVictory(arenaPlayers.get(0));
                onEnd();
            } else if (arenaPlayers.size() == 2) {
                for(Player ap: arenaPlayers){
                    ap.playSound(ap.getEyeLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1);
                    p.sendTitle(ChatColor.RED + "1 v 1", "",7, 16, 7);
                }
            }else{
                for(Player ap: arenaPlayers){
                    ap.playSound(ap.getEyeLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1);
                    p.sendTitle("", ChatColor.RED + String.valueOf(arenaPlayers.size()) + " players remaining",7, 16, 7);
                }
            }
        }else
            onRespawn(p);
    }

    private void onRespawn(Player p){
        p.setAllowFlight(true);
        p.setVelocity(new Vector(0,0,0));
        p.teleport(arena.getPlayerSpawns().get((int)(Math.random()*arena.getPlayerSpawns().size())).toBukkitLocation());
        p.setHealth(p.getHealth()-1);
        p.setAllowFlight(false);

        boolean voidSpawn = true;
        for(int b = 1; p.getLocation().getBlockY()-b >= 0; b++)
            if(p.getLocation().add(0,-b,0).getBlock().getType() != Material.AIR)
                voidSpawn=false;
        if(voidSpawn) p.getLocation().add(0,-1,0).getBlock().setType(Material.GOLD_BLOCK);

        for(Player dp: getPlavers(true))
            dp.sendMessage(ChatColor.DARK_RED + p.getName() + ChatColor.RED + " died!");

        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            p.setExp(0);
            p.setLevel(3);
        }, 3);
        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            p.setExp(0);
            p.setLevel(2);
        }, 20);
        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            p.setLevel(1);
        }, 40);
        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            p.setInvisible(false);
            p.setLevel(0);
        }, 60);
    }

    public void onVictory(Player p){
        p.playSound(p.getEyeLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.5f, 1);
        p.sendTitle(ChatColor.GOLD + "You Won!", "", 10, 30, 20);
        p.setMaxHealth(20);
        p.setHealthScale(20);
        p.setHealth(20);
        p.setExp(0);
        p.setLevel(0);
    }

    /// ON END
    private void teleportToLobby(Player p, Location loc){
        final int preEvac = 6;
        for(int i = 1; i < preEvac; i++){ int finalTime = i;
            Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () ->
            { p.sendMessage(ChatColor.GREEN + "Returning to lobby in " + finalTime); },
                    20*finalTime);
        }
        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            onLeave(p,loc);
        }, 20*6);

    }


    // Smash Player helpers
    public boolean player1HP(Player p){
        return p.getHealth()-1>0;
    }

    public void setPlayerDefault(Player p, GameMode mode){
        p.setGameMode(mode);
        p.setMaxHealth(20);
        p.setHealthScale(20);
        p.setHealth(20);
        p.setExp(0);
        p.setLevel(0);
        p.setFlying(false);
        p.setAllowFlight(false);
    }

    public void setPlayerStart(Player p){
        p.setLevel(0);
        p.setMaxHealth(getLives());
        p.setHealthScale(getLives());
        p.setHealth(getLives());
        p.getInventory().clear();
        p.setGameMode(GameMode.SURVIVAL);
        for(PotionEffect effect : p.getActivePotionEffects())
            p.removePotionEffect(effect.getType());
    }

}

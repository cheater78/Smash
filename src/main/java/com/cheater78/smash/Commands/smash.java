package com.cheater78.smash.Commands;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Conversion.LocationString;
import com.cheater78.smash.Events.FlightAttemp;
import com.cheater78.smash.Events.signChange;
import com.cheater78.smash.Files.ArenaFiles;
import com.cheater78.smash.Items.Items;
import com.cheater78.smash.Smash;
import com.cheater78.smash.World.RestoreWorld;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class smash implements TabExecutor {

    public static ArrayList<Arena> arProg = new ArrayList<>();
    public static Map<UUID, Map<Integer, ItemStack>> mainLobbyItems = new HashMap<>();
    public static Map<UUID, Float> mainLobbyLevels = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(args.length >0){
                Player p = (Player)sender;
                if(args[0].equalsIgnoreCase("join") && hasPerm(p,"smash.join")){
                    if(args.length > 1){
                        if(Arena.arenaOfPlayer(p,true) == null && Arena.arenaOfPlayer(p,false) == null){
                            for(Arena a: Smash.arenas){
                                if(a.name.equalsIgnoreCase(args[1])){
                                    if(!a.ready){
                                        p.sendMessage(ChatColor.RED + "Arena is currently rebuilding!");
                                        return true;
                                    }
                                    if(!a.gameActive){




                                    }else {
                                        p.sendMessage(ChatColor.RED + "Arena is currently running!");
                                    }
                                    return true;
                                }
                            }
                            p.sendMessage(ChatColor.RED + "Arena not available!(/smash list)");
                        }else{
                            p.sendMessage(ChatColor.RED + "You are currently in an Arena!(/smash leave)");
                        }
                    }else{
                        p.sendMessage(ChatColor.RED + "Wrong Usage!(/smash join [arena])");
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("leave") && hasPerm(p,"smash.leave")){
                    for(Arena a: Smash.arenas){
                        for(Player ap:a.arenaPlayer){
                            if(ap.getName().equalsIgnoreCase(p.getName())){
                                a.arenaPlayer.remove(p);
                                if(a.gameActive) FlightAttemp.onLeaveFlight(p);
                                if(a.arenaPlayer.isEmpty()){
                                    a.gameActive = false;
                                    FileConfiguration signConfig = signChange.getSignConfig();
                                    for (Location loc: LocationString.KeystoLocs(signConfig.getKeys(false))){
                                        if(signChange.getCommandFromSign(loc).contains("join")
                                                ||signChange.getCommandFromSign(loc).contains("spectate") ){
                                            Sign sign = (Sign) loc.getWorld().getBlockAt(loc).getState();
                                            if(signChange.getCommandFromSign(loc).contains("join"))
                                                sign.setLine(3,ChatColor.DARK_GRAY + "<" +ChatColor.GREEN+"join"+ChatColor.DARK_GRAY + ">");
                                            sign.setLine(1,ChatColor.DARK_GRAY+"Players: "+a.arenaPlayer.size());
                                            sign.update();
                                        }
                                    }
                                    Arena.clearEntities(a);
                                }
                                p.teleport(Smash.mainLobby);
                                if(p.getGameMode() == GameMode.SPECTATOR || p.getGameMode() == GameMode.ADVENTURE) p.setGameMode(GameMode.SURVIVAL);




                                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());


                                return true;
                            }
                        }
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("list") && hasPerm(p,"smash.list")){
                    p.sendMessage(ChatColor.GREEN + "Available Arenas: ");
                    if(Smash.arenas.isEmpty()){ p.sendMessage(ChatColor.RED + "[none]"); return true; }
                    for(Arena a: Smash.arenas){
                        if(!a.gameActive) p.sendMessage(ChatColor.GREEN + "  " + a.name);
                        else p.sendMessage(ChatColor.RED + "  " + a.name);
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("spectate") && hasPerm(p,"smash.spectate")){
                    if(args.length > 1){
                        if(Arena.arenaOfPlayer(p,true) == null && Arena.arenaOfPlayer(p,false) == null){
                            for(Arena a: Smash.arenas){
                                if(a.name.equalsIgnoreCase(args[1])){
                                    if(a.gameActive){





                                    }else {
                                        p.sendMessage(ChatColor.RED + "Arena is not running!");
                                    }
                                    return true;
                                }
                            }
                            p.sendMessage(ChatColor.RED + "Arena not available!(/smash list)");
                        }else{
                            p.sendMessage(ChatColor.RED + "You are currently in an Arena!(/smash leave)");
                        }
                    }else{
                        p.sendMessage(ChatColor.RED + "Wrong Usage!(/smash join [arena])");
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("settings") && hasPerm(p,"smash.settings")){
                    if(Arena.arenaOfPlayer(p,false) != null){
                        Arena a = Arena.arenaOfPlayer(p,false);
                        a.settingsGUI(p);
                    }else if(Arena.arenaOfPlayer(p,true) != null){
                        p.sendMessage(ChatColor.RED + "You cant change the settings while being in a Game!");
                    }else{
                        p.sendMessage(ChatColor.RED + "You need to be in a Game to change any settings!");
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("start") && hasPerm(p,"smash.start")){
                    for(Arena a: Smash.arenas){
                        for(Player ap:a.arenaPlayer){
                            if(ap.getName().equalsIgnoreCase(p.getName())){

                                FlightAttemp.start(a.arenaPlayer);


                                return true;
                            }
                        }
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("stop") && hasPerm(p,"smash.stop")){
                    for(Arena a: Smash.arenas){
                        for(Player ap:a.arenaPlayer){
                            if(ap.getName().equalsIgnoreCase(p.getName())){
                                FlightAttemp.stop(a.arenaPlayer);

                                return true;
                            }
                        }
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("config") && hasPerm(p,"smash.config")){
                    if(args.length < 2){ sender.sendMessage(ChatColor.RED + "Wrong usage!(/smash config [arena] [option])"); return true; }
                    if(!Smash.games.keySet().contains(args[2])){ sender.sendMessage(ChatColor.RED + "Arena: " + args[2] + " does not exist!"); return true; }

                    Smash.games.get(args[2]).onConfig(sender, args);
                    return true;

                }else if(args[0].equalsIgnoreCase("new") && hasPerm(p,"smash.new")){
                    if(!args[1].isEmpty()){
                        for(Arena a:arProg){
                            if(a.name.equalsIgnoreCase(args[1])){
                                p.sendMessage(ChatColor.RED + "Arena " + args[1] + " does already exist!");
                                return true;
                            }
                        }
                        p.sendTitle(ChatColor.DARK_RED + "Creating new Arena", ChatColor.GREEN + "generating...", 4, 10000000, 0);
                        if(Bukkit.getWorld(args[1]) == null){
                            WorldCreator wc = new WorldCreator(args[1]);
                            wc.environment(World.Environment.NORMAL);
                            wc.type(WorldType.FLAT);
                            wc.generatorSettings("{\"layers\": [], \"biome\":\"jungle\"}");
                            wc.createWorld();
                            Objects.requireNonNull(Bukkit.getWorld(args[1])).setKeepSpawnInMemory(false);
                            if(p.getGameMode() == GameMode.CREATIVE)
                                p.setFlying(true);
                        }
                        arProg.add(new Arena(args[1], false));
                        Objects.requireNonNull(Bukkit.getWorld(args[1])).setGameRule(GameRule.DO_MOB_SPAWNING, false);
                        p.teleport(new Location(Bukkit.getWorld(args[1]), 0, 100, 0, 0, 0));
                        p.sendTitle("","",0,1,0);
                        p.sendMessage(ChatColor.GREEN +"Arena " + args[1] + " created!");
                        return true;
                    }else{
                        p.sendMessage(ChatColor.RED +"Usage: /smash new [ArenaName]");
                    }
                }else if(args[0].equalsIgnoreCase("save") && hasPerm(p,"smash.save")){
                    if(args.length > 1){

                        p.sendMessage(ChatColor.RED + "Arena not found!");
                    }else{
                        p.sendMessage(ChatColor.RED + "Wrong Usage!(/smash save [arena])");
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("delete") && hasPerm(p,"smash.delete")){
                    if(args.length > 1){
                        for(Arena a:arProg){
                            if(a.name.equalsIgnoreCase(args[1]) || ArenaFiles.getFile().get(a.name) != null){
                                Arena.delete(a.name);
                                p.sendMessage(ChatColor.DARK_RED + "Arena deleted!");
                                p.sendMessage(ChatColor.GREEN + "Undo with /smash save [arena](Irreversible after /smash reset)");
                            }else {
                                p.sendMessage(ChatColor.RED + "Arena does not exist!");
                            }
                            return true;
                        }
                    }else{
                        p.sendMessage(ChatColor.RED + "Wrong Usage!(/smash delete [arena])");
                        return true;
                    }
                }else if(args[0].equalsIgnoreCase("reset") && hasPerm(p,"smash.reset")){
                    for(Arena a:Smash.arenas){
                        for(Player rap: a.arenaPlayer){
                            rap.sendMessage(ChatColor.DARK_RED + "Smash PLugin RESET! - returning to lobby...)");
                            rap.teleport(Smash.mainLobby);
                            rap.getInventory().clear();
                            for(Integer i = 0; i < mainLobbyItems.get(rap.getUniqueId()).size(); i++)
                                rap.getInventory().setItem(i, mainLobbyItems.get(rap.getUniqueId()).get(i));
                            rap.setLevel((int)mainLobbyLevels.get(rap.getUniqueId()).floatValue());
                            rap.setExp(mainLobbyLevels.get(rap.getUniqueId()) %1);
                        }
                    }
                    smash.arProg.clear();
                    Smash.arenas.clear();
                    for(String s: Arena.listArena()) smash.arProg.add(new Arena(s, true));
                    for(String s: Arena.listArena()) Smash.arenas.add(new Arena(s, true));
                    mainLobbyLevels.clear();
                    mainLobbyItems.clear();
                    p.sendMessage(ChatColor.DARK_RED + "RESET! - loaded files(non saved arenas are gone)");
                    return true;
                }else return false;
            }
        }else{
            System.out.println("This Command can only be executed as Player!");
            return true;
        }
        return false;
    }




    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> ret = new ArrayList<>();

        if(sender instanceof Player){
            Player p = (Player) sender;
            if (args.length == 1){
                if(p.hasPermission("smash.join") && (Arena.arenaOfPlayer(p,true) == null && Arena.arenaOfPlayer(p,false) == null)) ret.add("join");
                if(p.hasPermission("smash.leave") && (Arena.arenaOfPlayer(p,true) != null || Arena.arenaOfPlayer(p,false) != null)) ret.add("leave");
                if(p.hasPermission("smash.spectate") && (Arena.arenaOfPlayer(p,true) == null && Arena.arenaOfPlayer(p,false) == null)) ret.add("spectate");
                if(p.hasPermission("smash.list")) ret.add("list");
                if(p.hasPermission("smash.settings") && Arena.arenaOfPlayer(p,false) != null) ret.add("settings");
                if(p.hasPermission("smash.start") && Arena.arenaOfPlayer(p,false) != null) ret.add("start");
                if(p.hasPermission("smash.stop") && Arena.arenaOfPlayer(p,true) != null) ret.add("stop");
                if(p.hasPermission("smash.config") && (Arena.arenaOfPlayer(p,true) == null && Arena.arenaOfPlayer(p,false) == null)) ret.add("config");
                if(p.hasPermission("smash.new") && (Arena.arenaOfPlayer(p,true) == null && Arena.arenaOfPlayer(p,false) == null)) ret.add("new");
                if(p.hasPermission("smash.save") && (Arena.arenaOfPlayer(p,true) == null && Arena.arenaOfPlayer(p,false) == null)) ret.add("save");
                if(p.hasPermission("smash.delete") && (Arena.arenaOfPlayer(p,true) == null && Arena.arenaOfPlayer(p,false) == null)) ret.add("delete");
                if(p.hasPermission("smash.reset")) ret.add("reset");
            }
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("join")){
                    for(Arena a:Smash.arenas){
                        ret.add(a.name);
                    }
                }else if(args[0].equalsIgnoreCase("spectate")){
                    for(Arena a:Smash.arenas){
                        if(a.gameActive)
                            ret.add(a.name);
                    }
                }else if(args[0].equalsIgnoreCase("config")){
                    for(Arena a:arProg){
                        ret.add(a.name);
                    }
                }else if(args[0].equalsIgnoreCase("save")){
                    for(Arena a:arProg){
                        ret.add(a.name);
                    }
                }else if(args[0].equalsIgnoreCase("delete")){
                    for(Arena a:Smash.arenas){
                        ret.add(a.name);
                    }
                }

            }
            if(args.length == 3){
                if(args[0].equalsIgnoreCase("config")){
                    if(p.hasPermission("smash.config.pos1")) ret.add("pos1");
                    if(p.hasPermission("smash.config.pos2")) ret.add("pos2");
                    if(p.hasPermission("smash.config.setLobbySpawn")) ret.add("setLobbySpawn");
                    if(p.hasPermission("smash.config.setSpecSpawn")) ret.add("setSpecSpawn");
                    if(p.hasPermission("smash.config.setPlayerSpawn")) ret.add("setPlayerSpawn");
                    if(p.hasPermission("smash.config.setItemSpawn")) ret.add("setItemSpawn");
                }
            }
            if(args.length == 4){
                if(args[2].equalsIgnoreCase("setPlayerSpawn") || args[2].equalsIgnoreCase("setItemSpawn")){
                    ret.add("4");
                }
            }
        }

        return ret;
    }

    public static boolean hasPerm(CommandSender s, String permission){
        if(!(s instanceof Player))
            return true;
        Player p = (Player) s;
        if(!p.hasPermission(permission)){
            p.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
        }
        return p.hasPermission(permission);
    }

}

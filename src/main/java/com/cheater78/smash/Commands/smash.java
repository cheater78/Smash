package com.cheater78.smash.Commands;

import com.cheater78.smash.Game.Elements.GUI.InventoryGUI;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Game.Systems.PlayerManager;
import com.cheater78.smash.Smash;
import com.cheater78.smash.Utils.BukkitWorldLoader;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class smash implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            printHelp(sender);
            return true;
        }
        //Player only Cmds: join, leave, spectate, settings
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("join") && hasPerm(p, "smash.join")) {
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Wrong Usage!(/smash join [game])");
                    return true;
                }
                if (PlayerManager.isInGame(p)) {
                    p.sendMessage(ChatColor.RED + "You are currently in a Game!(/smash leave)");
                    return true;
                }
                PlayerManager.getGame(p).onJoin(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("leave") && hasPerm(p, "smash.leave")) {
                if (!PlayerManager.isInGame(p)) {
                    p.sendMessage(ChatColor.RED + "You aren't currently in a Game!");
                    return true;
                }
                PlayerManager.getGame(p).onLeave(p, PlayerManager.getGame(p).getArena().getEvacuationPoint());
                return true;
            }
            if (args[0].equalsIgnoreCase("spectate") && hasPerm(p, "smash.spectate")) {
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Wrong Usage!(/smash spectate [arena])");
                    return true;
                }
                if (PlayerManager.isInGame(p)) {
                    p.sendMessage(ChatColor.RED + "You are currently a Game!(/smash leave)");
                    return true;
                }
                PlayerManager.getGame(p).onSpectate(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("settings") && hasPerm(p, "smash.settings")) {
                if (!PlayerManager.isInGame(p)) {
                    p.sendMessage(ChatColor.RED + "You need to be in a Lobby to change Game Settings!");
                    return true;
                }
                if (PlayerManager.getGame(p).isGameActive()) {
                    p.sendMessage(ChatColor.RED + "You can't change Game Settings while the Game is in progress!");
                    return true;
                }
                InventoryGUI.settingsGUI(p, PlayerManager.getGame(p));
                return true;
            }
            if (args[0].equalsIgnoreCase("start") && hasPerm(sender, "smash.start.self")) {
                if (!PlayerManager.isInGame(p)) {
                    p.sendMessage(ChatColor.RED + "You aren't currently in a Game!");
                    return true;
                }
                if (PlayerManager.getGame(p).isGameActive()) {
                    p.sendMessage(ChatColor.RED + "Game has already started!");
                    return true;
                }
                PlayerManager.getGame(p).onStart(p);
            }
            if (args[0].equalsIgnoreCase("stop") && hasPerm(sender, "smash.stop.self")) {
                if (!PlayerManager.isInGame(p)) {
                    p.sendMessage(ChatColor.RED + "You aren't currently in a Game!");
                    return true;
                }
                if (!PlayerManager.getGame(p).isGameActive()) {
                    p.sendMessage(ChatColor.RED + "Game is currently not running!");
                    return true;
                }
                PlayerManager.getGame(p).onStop();
            }
        }

        //other Cmds: new, config, reset, list, remove, | start, stop
        if (args[0].equalsIgnoreCase("list") && hasPerm(sender, "smash.list")) {
            sender.sendMessage(ChatColor.GREEN + "Available Arenas: ");
            if (Smash.games.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "[none]");
                return true;
            }
            for (SmashGame g : Smash.games.values()) {
                if (g.isGameActive()) sender.sendMessage(ChatColor.GOLD + "  " + g.getArena().getName() + "(running)");
                else if (!g.isReady())
                    sender.sendMessage(ChatColor.RED + "  " + g.getArena().getName() + "(unavailable)");
                else sender.sendMessage(ChatColor.GREEN + "  " + g.getArena().getName());
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("start") && hasPerm(sender, "smash.start.other")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Wrong Usage!(/smash start [game])");
                return true;
            }
            if (!Smash.games.containsKey(args[1])) {
                sender.sendMessage(ChatColor.RED + "Arena " + args[1] + " does not exist!");
                return true;
            }
            Smash.games.get(args[1]).onStart(null);
        }
        if (args[0].equalsIgnoreCase("stop") && hasPerm(sender, "smash.stop.other")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Wrong Usage!(/smash stop [game])");
                return true;
            }
            if (!Smash.games.containsKey(args[1])) {
                sender.sendMessage(ChatColor.RED + "Arena " + args[1] + " does not exist!");
                return true;
            }
            Smash.games.get(args[1]).onStart(null);
        }
        if (args[0].equalsIgnoreCase("config") && hasPerm(sender, "smash.config")) {
            Smash.games.get(args[2]).onConfig(sender, args);
        }
        if (args[0].equalsIgnoreCase("new") && hasPerm(sender, "smash.new")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Wrong Usage!(/smash new [newGameName])");
                return true;
            }
            if (Smash.games.containsKey(args[1])) {
                sender.sendMessage(ChatColor.RED + "Game " + args[1] + " does already exist!");
                return true;
            }
            //TODO: message or smth
            Smash.games.put(args[1], new SmashGame(args[1], args[1], false));
        }
        if (args[0].equalsIgnoreCase("save") && hasPerm(sender, "smash.save")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Wrong Usage!(/smash save [game])");
                return true;
            }
            if (!Smash.games.containsKey(args[1])) {
                sender.sendMessage(ChatColor.RED + "Game " + args[1] + " does not exist!");
                return true;
            }
            Smash.games.get(args[1]).onSave(sender);
        }
        if (args[0].equalsIgnoreCase("remove") && hasPerm(sender, "smash.remove")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Wrong Usage!(/smash remove [game])");
                return true;
            }
            if (!Smash.games.containsKey(args[1])) {
                sender.sendMessage(ChatColor.RED + "Game " + args[1] + " does not exist!");
                return true;
            }
            Smash.games.get(args[1]).onDelete();
            BukkitWorldLoader.deleteWorld(args[1]);
            //TODO: message or smth
        }
        if (args[0].equalsIgnoreCase("reset") && hasPerm(sender, "smash.reset")) {
            //Evacuation
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (!PlayerManager.isInGame(p)) continue;
                p.sendMessage(ChatColor.DARK_RED + "Smash Plugin RESET! - evacuating...)");
                p.teleport(PlayerManager.getGame(p).getArena().getEvacuationPoint().toBukkitLocation());
                Smash.itemSaver.onLeave(p);
            }
            //RESET
            Smash.games.clear();
            Smash.init();
            //TODO: Check all systems

            sender.sendMessage(ChatColor.DARK_RED + "RESET! - reloaded all files -> (non saved Games are gone)");
        }

        printHelp(sender);
        return true;
    }


    /*
    Permissions:

    [Player]
    smash.join
    smash.leave
    smash.spectate
    smash.list
    smash.settings
    smash.start.self
    smash.stop.self

    [Admin]
    smash.new
    smash.config
    smash.save
    smash.remove
    smash.reset
    smash.start.other
    smash.stop.other

     */

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> ret = new ArrayList<>();

        if(sender instanceof Player){
            Player p = (Player) sender;
            if (args.length == 1){
                if(p.hasPermission("smash.join") && (!PlayerManager.isInGame(p))) ret.add("join");
                if(p.hasPermission("smash.leave") && (PlayerManager.isInGame(p))) ret.add("leave");
                if(p.hasPermission("smash.spectate") && (!PlayerManager.isInGame(p))) ret.add("spectate");
                if(p.hasPermission("smash.list")) ret.add("list");
                if(p.hasPermission("smash.settings") && (PlayerManager.isInGame(p))) ret.add("settings");
                if(p.hasPermission("smash.start") && (PlayerManager.isInGame(p))) ret.add("start");
                if(p.hasPermission("smash.stop") && (PlayerManager.isInGame(p))) ret.add("stop");
                if(p.hasPermission("smash.config") && (!PlayerManager.isInGame(p))) ret.add("config");
                if(p.hasPermission("smash.new") && (!PlayerManager.isInGame(p))) ret.add("new");
                if(p.hasPermission("smash.save") && (!PlayerManager.isInGame(p))) ret.add("save");
                if(p.hasPermission("smash.remove") && (!PlayerManager.isInGame(p))) ret.add("remove");
                if(p.hasPermission("smash.reset")) ret.add("reset");
            }
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("join")
                || args[0].equalsIgnoreCase("spectate")
                || args[0].equalsIgnoreCase("config")
                || args[0].equalsIgnoreCase("save")
                || args[0].equalsIgnoreCase("remove"))
                {
                    ret.addAll(Smash.games.keySet());
                }
            }
            if(args.length == 3){
                if(args[0].equalsIgnoreCase("config")){
                    if(p.hasPermission("smash.config.pos1")) ret.add("pos1");
                    if(p.hasPermission("smash.config.pos2")) ret.add("pos2");
                    if(p.hasPermission("smash.config.setLobbySpawn")) ret.add("setLobbySpawn");
                    if(p.hasPermission("smash.config.setSpecSpawn")) ret.add("setSpecSpawn");
                    if(p.hasPermission("smash.config.setPlayerSpawn")) ret.add("addPlayerSpawn");
                    if(p.hasPermission("smash.config.setItemSpawn")) ret.add("addItemSpawn");
                    if(p.hasPermission("smash.config.remPlayerSpawn")) ret.add("remPlayerSpawn");
                    if(p.hasPermission("smash.config.remItemSpawn")) ret.add("remItemSpawn");
                }
            }

            if(args.length == 4){
                if(args[3].equalsIgnoreCase("remPlayerSpawn")){
                    if(!Smash.games.containsKey(args[1])) return ret;
                    for (int i = 0; i < Smash.games.get(args[1]).getArena().getPlayerSpawns().size(); i++){
                        ret.add(Integer.toString(i));
                    }
                }
                if(args[3].equalsIgnoreCase("remItemSpawn")){
                    if(!Smash.games.containsKey(args[1])) return ret;
                    for (int i = 0; i < Smash.games.get(args[1]).getArena().getItemSpawns().size(); i++){
                        ret.add(Integer.toString(i));
                    }
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

    private void printHelp(CommandSender s){
        //TODO: implementHelp
        s.sendMessage("Sorry still nothing here");
    }

}

package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Conversion.LocationString;
import com.cheater78.smash.Smash;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.io.File;
import java.io.IOException;

public class signChange implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("smash.signs.create") && p.getGameMode() == GameMode.CREATIVE && Smash.useSigns){
            Sign s = (Sign) e.getBlock().getState();

            if(e.getLine(0).equalsIgnoreCase("[smash]")){
                File signFile = new File(Bukkit.getServer().getPluginManager().getPlugin("Smash").getDataFolder(),"Signs.yml");
                if(!signFile.exists()){
                    try {
                        signFile.createNewFile();
                    }catch (IOException ignored){System.out.println("SignFile cannot be created, but is necessary!");}
                }
                FileConfiguration signConfig = YamlConfiguration.loadConfiguration(signFile);


                if(e.getLine(1).equalsIgnoreCase("join")){


                    for(Arena a: Smash.arenas){
                        if(e.getLine(2).equalsIgnoreCase(a.name)){
                            signConfig.set(LocationString.LocToString(s.getLocation()), "join" + " " + a.name);
                            try {
                                signConfig.save(signFile);
                            }catch (IOException ignored){ System.out.println(" Saving SignFile failed!"); }
                            s.setLine(0, ChatColor.BLACK+"["+ChatColor.GOLD+"Smash"+ChatColor.BLACK+" - "+ChatColor.WHITE+a.name+ChatColor.BLACK+"]");
                            s.setLine(1,ChatColor.DARK_GRAY+"Players: "+a.arenaPlayer.size());
                            s.setLine(3,ChatColor.DARK_GRAY + "<" +ChatColor.GREEN+"Join"+ChatColor.DARK_GRAY + ">");
                            e.setCancelled(true);
                            s.update();
                            return;
                        }
                    }
                    s.getBlock().setType(Material.AIR);
                    p.sendMessage(ChatColor.RED + "Arena \"" + s.getLine(2) + "\" doesn't exist");


                }else if(e.getLine(1).equalsIgnoreCase("leave")){


                    s.setLine(0, ChatColor.BLACK+"["+ChatColor.GOLD+"Smash"+ChatColor.BLACK+"]");
                    s.setLine(2,ChatColor.DARK_GRAY + "<" +ChatColor.GREEN+"Leave"+ChatColor.DARK_GRAY + ">");
                    e.setCancelled(true);
                    s.update();
                    signConfig.set(LocationString.LocToString(s.getLocation()), "leave");


                }else if(e.getLine(1).equalsIgnoreCase("spectate")){


                    for(Arena a: Smash.arenas){
                        if(e.getLine(2).equalsIgnoreCase(a.name)){
                            signConfig.set(LocationString.LocToString(s.getLocation()), "spectate" + " " + a.name);
                            try {
                                signConfig.save(signFile);
                            }catch (IOException ignored){ System.out.println(" Saving SignFile failed!"); }
                            s.setLine(0, ChatColor.BLACK+"["+ChatColor.GOLD+"Smash"+ChatColor.BLACK+" - "+ChatColor.WHITE+a.name+ChatColor.BLACK+"]");
                            s.setLine(1,ChatColor.DARK_GRAY+"Players: "+a.arenaPlayer.size());
                            s.setLine(3,ChatColor.DARK_GRAY + "<" +ChatColor.GREEN+"Spectate"+ChatColor.DARK_GRAY + ">");
                            e.setCancelled(true);
                            s.update();
                            return;
                        }
                    }
                    s.getBlock().setType(Material.AIR);
                    p.sendMessage(ChatColor.RED + "Arena \"" + s.getLine(2) + "\" doesn't exist");


                }else if(e.getLine(1).equalsIgnoreCase("settings")){


                    s.setLine(0, ChatColor.BLACK+"["+ChatColor.GOLD+"Smash"+ChatColor.BLACK+"]");
                    s.setLine(2,ChatColor.DARK_GRAY + "<" +ChatColor.GREEN+"Settings"+ChatColor.DARK_GRAY + ">");
                    e.setCancelled(true);
                    s.update();
                    signConfig.set(LocationString.LocToString(s.getLocation()), "settings");


                }else if(e.getLine(1).equalsIgnoreCase("start")){


                    s.setLine(0, ChatColor.BLACK+"["+ChatColor.GOLD+"Smash"+ChatColor.BLACK+"]");
                    s.setLine(2,ChatColor.DARK_GRAY + "<" +ChatColor.RED+"Start"+ChatColor.DARK_GRAY + ">");
                    e.setCancelled(true);
                    s.update();
                    signConfig.set(LocationString.LocToString(s.getLocation()), "start");


                }else{
                    p.sendMessage(ChatColor.RED + "Wrong Usage!(Sign:{ Line1: [smash] , Line2: join/leave/spectate/settings/start })");
                    e.getBlock().setType(Material.AIR);
                    return;
                }

                try {
                    signConfig.save(signFile);
                }catch (IOException ignored){ System.out.println(" Saving SignFile failed!"); }
            }
        }
    }

    public static String getCommandFromSign(Location loc){
        String command;

        File signFile = new File(Bukkit.getServer().getPluginManager().getPlugin("Smash").getDataFolder(),"Signs.yml");
        if(!signFile.exists()){
            try {
                signFile.createNewFile();
            }catch (IOException ignored){System.out.println("SignFile cannot be created, but is necessary!");}
        }
        FileConfiguration signConfig = YamlConfiguration.loadConfiguration(signFile);

        command = (String) signConfig.get(LocationString.LocToString(loc));

        if(command != null) return ("smash " + command );
        else return null;
    }

    public static FileConfiguration getSignConfig(){
        File signFile = new File(Bukkit.getServer().getPluginManager().getPlugin("Smash").getDataFolder(),"Signs.yml");
        if(!signFile.exists()){
            try {
                signFile.createNewFile();
            }catch (IOException ignored){System.out.println("SignFile cannot be created, but is necessary!");}
        }
        FileConfiguration signConfig = YamlConfiguration.loadConfiguration(signFile);

        return signConfig;
    }
}

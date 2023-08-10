package com.cheater78.smash.Utils;

import com.cheater78.smash.Serialize.Serializable.Location;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BukkitWorldLoader {

    public static void backupWorld(World world, String suffix, boolean save){
        copyWorld(world, world.getName()+suffix, save);
    }

    public static void loadBackup(String world, String suffix, boolean save){
        copyWorld(getWorld(world+suffix), world, save);
    }

    public static World getWorld(String worldName){
        if(worldName == null) throw new NullPointerException();
        if(worldName.equals("")) throw new IllegalArgumentException();

        File worldFolder = new File(Bukkit.getWorldContainer().getAbsolutePath() + worldName);
        if(!worldFolder.exists()) Bukkit.getLogger().info("World: " + worldName + " doesnt exist, Creating new...");

        if(!isWorldLoaded(worldName))
            return Bukkit.createWorld(new WorldCreator(worldName));
        else
            return Bukkit.getWorld(worldName);
    }


    public static void unloadWorld(World world, boolean save) {
        if(world == null) throw new NullPointerException();
        unloadWorld(world.getName(), save);
    }

    public static void unloadWorld(String worldName, boolean save) {
        if(worldName == null) throw new NullPointerException();
        if(worldName.equals("")) throw new IllegalArgumentException();
        if(!Bukkit.getServer().unloadWorld(worldName, save))
            Bukkit.getLogger().warning("World: " + worldName + " could not be unloaded!");
    }

    public static void evacuateWorld(World world, Location evacuationDest){
        if(evacuationDest == null) throw new IllegalStateException();
        org.bukkit.Location dest = evacuationDest.toBukkitLocation();
        for(Player p : world.getPlayers()){
            p.sendMessage(ChatColor.RED + "Evacuating... Server is shutting down!");
            p.teleport(dest);
        }
    }

    private static boolean isWorldLoaded(String worldName){
        try{                        Bukkit.getWorld(worldName).getName();   }
        catch (Exception e){        return false;                           }
        return true;
    }

    public static void copyWorld(World originalWorld, String newWorldName, boolean save) {
        if(originalWorld == null) throw new NullPointerException("CopyWorld was called with null");
        if(isWorldLoaded(originalWorld.getName())) unloadWorld(originalWorld, save);
        if(isWorldLoaded(newWorldName)) unloadWorld(newWorldName, false);
        copy(originalWorld.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorldName));
        Bukkit.createWorld(new WorldCreator(newWorldName));
    }

    private static void copy(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Copy: Couldn't create world directory!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copy(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

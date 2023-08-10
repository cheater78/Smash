package com.cheater78.smash.Config;

import com.cheater78.smash.Serialize.Serializable.Location;
import com.cheater78.smash.Smash;
import com.cheater78.smash.Utils.BukkitWorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginConfig {

    private static File configFile;
    private static FileConfiguration config;

    public static int defaultLives = 6;
    public static int defaultMaxDamage = 600;
    public static boolean useSigns = true;
    public static boolean useScoreboards = true;
    public static boolean useLobbyItems = true;
    public static Location defaultEvacPoint = new Location(BukkitWorldLoader.getWorld("world").getSpawnLocation());

    public static void load() {
        if(!configFile.exists()) throw new IllegalStateException("Loading Config.yml failed(run init() first)");
        config = YamlConfiguration.loadConfiguration(configFile);
        defaultLives = (int) config.get("defaultLives");
        defaultMaxDamage = (int) config.get("defaultMaxDamage");
        useSigns = (boolean) config.get("useSigns");
        useScoreboards = (boolean) config.get("useScoreboards");
        useLobbyItems = (boolean) config.get("useLobbyItems");
        defaultEvacPoint = (Location) config.get("defaultEvacPoint");
    }

    public static void store(){
        try {
            config.set("defaultLives", defaultLives);
            config.set("defaultMaxDamage", defaultMaxDamage);
            config.set("useSigns", useSigns);
            config.set("useScoreboards", useScoreboards);
            config.set("useLobbyItems", useLobbyItems);
            config.set("defaultEvacPoint", defaultEvacPoint);
            config.save(configFile);
        }catch (Exception e){
            Smash.log.severe("Config.yml cannot be written!");
        }
    }

    public static void init(){
        configFile = new File(Smash.plugin.getDataFolder(), "Config.yml");
        if(!configFile.exists()){
            try {
                Smash.log.info("Config.yml not found, creating new one...");
                configFile.createNewFile();
                config = YamlConfiguration.loadConfiguration(configFile);
                store();
            } catch (IOException e) {
                Smash.log.severe("Config.yml cannot be created!");
            }
        }
    }
}

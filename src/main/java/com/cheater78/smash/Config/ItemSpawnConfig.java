package com.cheater78.smash.Config;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Elements.Items;
import com.cheater78.smash.Smash;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemSpawnConfig {

    private static File configFile;
    private static FileConfiguration config;

    private static Map<String, Integer> distribution = new HashMap<>();

    public static void load() {
        if(!configFile.exists()) throw new IllegalStateException("Loading ItemDistribution.yml failed(run init() first)");
        config = YamlConfiguration.loadConfiguration(configFile);
        for(String str : config.getKeys(false)){
            distribution.put(str, (int) config.get(str));
        }
    }

    public static void store(){
        try {
            for(String str : distribution.keySet()){
                config.set(str, distribution.get(str));
            }
            config.save(configFile);
        }catch (Exception e){
            Smash.plugin.getLogger().severe("ItemDistribution.yml cannot be written!");
        }
    }

    public static void init(){
        configFile = new File(Smash.plugin.getDataFolder(), "ItemDistribution.yml");
        if(!configFile.exists()){
            try {
                Smash.log.info("ItemDistribution.yml not found, creating new one...");
                configFile.createNewFile();
                config = YamlConfiguration.loadConfiguration(configFile);
                for(Item item : Items.getAll())
                    distribution.put(item.getName(), 1);
                store();
            } catch (IOException e) {
                Smash.plugin.getLogger().severe("ItemDistribution.yml cannot be created!");
            }
        }
    }

    public static int getItemOccurence(Item item){
        return distribution.get(item.getName());
    }

}

package com.cheater78.smash.Config;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Elements.Items;
import com.cheater78.smash.Smash;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GameConfig {


    private static File configFile;
    private static FileConfiguration config;

    public static int smasherDmg = 60;
    public static double playerDmgMul = 7;
    public static double entityDmgMul = 4;
    public static double fallDmgMul = 0.8;
    public static double fireDmgMul = 5;
    public static double entityExplosionDmgMul = 3;

    public static double playerKnockbackMul = 0.08333;
    public static double entityKnockbackMul = 0.0625;
    public static double entityExplosionKnockbackMul = 0.2;
    public static double blockExplosionKnockbackMul = 0.2;

    public static double grapplingHookMul = 1;


    public static void load() {
        if(!configFile.exists()) throw new IllegalStateException("Loading GameConfig.yml failed(run init() first)");
        config = YamlConfiguration.loadConfiguration(configFile);
        smasherDmg = (int) config.get("smasherDmg");
        playerDmgMul = (double) config.get("playerDmgMul");
        entityDmgMul = (double) config.get("entityDmgMul");
        fallDmgMul = (double) config.get("fallDmgMul");
        fireDmgMul = (double) config.get("fireDmgMul");
        entityExplosionDmgMul = (double) config.get("entityExplosionDmgMul");
        playerKnockbackMul = (double) config.get("playerKnockbackMul");
        entityKnockbackMul = (double) config.get("entityKnockbackMul");
        entityExplosionKnockbackMul = (double) config.get("entityExplosionKnockbackMul");
        blockExplosionKnockbackMul = (double) config.get("blockExplosionKnockbackMul");

    }

    public static void store(){
        try {
            config.set("smasherDmg", smasherDmg);
            config.set("playerDmgMul", playerDmgMul);
            config.set("entityDmgMul", entityDmgMul);
            config.set("fallDmgMul", fallDmgMul);
            config.set("fireDmgMul", fireDmgMul);
            config.set("entityExplosionDmgMul", entityExplosionDmgMul);
            config.set("playerKnockbackMul", playerKnockbackMul);
            config.set("entityKnockbackMul", entityKnockbackMul);
            config.set("entityExplosionKnockbackMul", entityExplosionKnockbackMul);
            config.set("blockExplosionKnockbackMul", blockExplosionKnockbackMul);
            config.save(configFile);
        }catch (Exception e){
            Smash.plugin.getLogger().severe("GameConfig.yml cannot be written!");
        }
    }

    public static void init(){
        configFile = new File(Smash.plugin.getDataFolder(), "GameConfig.yml");
        if(!configFile.exists()){
            try {
                Smash.log.info("GameConfig.yml not found, creating new one...");
                configFile.createNewFile();
                config = YamlConfiguration.loadConfiguration(configFile);
                store();
            } catch (IOException e) {
                Smash.plugin.getLogger().severe("GameConfig.yml cannot be created!");
            }
        }
    }
}

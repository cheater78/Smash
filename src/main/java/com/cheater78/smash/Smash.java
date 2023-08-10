package com.cheater78.smash;

import com.cheater78.smash.API.RecoverItems;
import com.cheater78.smash.Commands.smash;
import com.cheater78.smash.Config.ItemSpawnConfig;
import com.cheater78.smash.Config.PluginConfig;
import com.cheater78.smash.Events.*;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Utils.RuntimeItemSaver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public final class Smash extends JavaPlugin {

    public static Plugin plugin;
    public static BukkitScheduler sceduler;
    public static final String worlBackupSuffix = "-SMASH-TEMPLATE";

    public static Map<String, SmashGame> games;

    public static final Logger log = Bukkit.getLogger();

    //Restore Items between Games
    public static RecoverItems itemSaver = new RuntimeItemSaver();


    @Override
    public void onEnable() {
        Objects.requireNonNull(this.getCommand("smash")).setExecutor(new smash());
        this.getServer().getPluginManager().registerEvents(new EntityDamageEventClass(), this);
        this.getServer().getPluginManager().registerEvents(new Hunger(), this);
        this.getServer().getPluginManager().registerEvents(new Leave(), this);
        this.getServer().getPluginManager().registerEvents(new Regen(), this);
        this.getServer().getPluginManager().registerEvents(new Move(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerPlaceBlock(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerPickUp(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        this.getServer().getPluginManager().registerEvents(new FlightAttemp(), this);
        this.getServer().getPluginManager().registerEvents(new signChange(), this);
        this.getServer().getPluginManager().registerEvents(new BlockExplode(), this);
        this.getServer().getPluginManager().registerEvents(new EntitySpawn(), this);
        this.getServer().getPluginManager().registerEvents(new Fishing(), this);


        plugin = this;
        sceduler = Bukkit.getServer().getScheduler();

        PluginConfig.init();
        PluginConfig.load();

        ItemSpawnConfig.init();
        ItemSpawnConfig.load();


        //LOAD Games / Arenas

        InventoryClick.updateSettingsAndScoreboardAndSigns();


        Smash.log.info("[Smash by c78] starting...");
    }

    @Override
    public void onDisable() {
        System.out.println("Smash stopped!");
    }
}

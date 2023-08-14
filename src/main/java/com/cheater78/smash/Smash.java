package com.cheater78.smash;

import com.cheater78.smash.API.RecoverItems;
import com.cheater78.smash.Commands.smash;
import com.cheater78.smash.Config.GameConfig;
import com.cheater78.smash.Config.ItemSpawnConfig;
import com.cheater78.smash.Config.PluginConfig;
import com.cheater78.smash.Events.*;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Game.Systems.PlayerManager;
import com.cheater78.smash.Utils.RuntimeItemSaver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public final class Smash extends JavaPlugin {

    public static Plugin plugin;
    public static BukkitScheduler sceduler;
    public static Logger log;
    public static final String worlBackupSuffix = "-SMASH-TEMPLATE";

    public static Map<String, SmashGame> games = new HashMap<>();
    //Restore Items between Games
    public static RecoverItems itemSaver;


    public static void init(){
        itemSaver = new RuntimeItemSaver();

        PluginConfig.init();
        PluginConfig.load();

        GameConfig.init();
        GameConfig.load();

        ItemSpawnConfig.init();
        ItemSpawnConfig.load();

        PlayerManager.init();
    }

    @Override
    public void onEnable() {
        plugin = this;
        sceduler = Bukkit.getServer().getScheduler();
        log = Bukkit.getLogger();
        init();


        Objects.requireNonNull(this.getCommand("smash")).setExecutor(new smash());

        PluginManager plman = Bukkit.getServer().getPluginManager();

        plman.registerEvents(new BlockExplode(), plugin);
        plman.registerEvents(new EntityDamageEventClass(), plugin);
        plman.registerEvents(new Leave(), plugin);
        plman.registerEvents(new Move(), plugin);
        plman.registerEvents(new PlayerInteract(), plugin);
        plman.registerEvents(new PlayerPlaceBlock(), plugin);
        plman.registerEvents(new InventoryClick(), plugin);
        plman.registerEvents(new PlayerPickUp(), plugin);
        plman.registerEvents(new FlightAttemp(), plugin);
        plman.registerEvents(new signChange(), plugin);
        plman.registerEvents(new EntitySpawn(), plugin);
        plman.registerEvents(new Fishing(), plugin);

        plman.registerEvents(new PlayerManager(), plugin);







        //LOAD Games / Arenas

        InventoryClick.updateSettingsAndScoreboardAndSigns();


        Smash.log.info("[Smash by c78] starting...");
    }

    @Override
    public void onDisable() {
        System.out.println("[Smash by c78] stopped!");
    }
}

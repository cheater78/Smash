package com.cheater78.smash.Game.Systems;

import com.cheater78.smash.Game.SmashGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager implements Listener {
    private static final Map<Player, SmashGame> players = new HashMap<>();

    public static void init(){
        for(Player player: Bukkit.getServer().getOnlinePlayers())
            players.put(player, null);
    }

    public static boolean isInGame(Player p){
        return players.get(p) != null;
    }

    public static void setGame(Player p, SmashGame s){
        players.put(p, s);
    }

    public static SmashGame getGame(Player p){
        return players.get(p);
    }

    public static void remGame(Player p){
        players.put(p, null);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent e){
        players.put(e.getPlayer(), null);
    }
    @EventHandler
    void onLeave(PlayerQuitEvent e){
        players.remove(e.getPlayer());
    }

}

package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Conversion.LocationString;
import com.cheater78.smash.Smash;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Leave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(Arena.arenaOfPlayer(p,true) != null){
            Arena a = Arena.arenaOfPlayer(p,true);
            a.arenaPlayer.remove(p);
            FlightAttemp.onLeaveFlight(p);
            if(a.arenaPlayer.isEmpty()){
                a.gameActive = false;
                FileConfiguration signConfig = signChange.getSignConfig();
                for (Location loc: LocationString.KeystoLocs(signConfig.getKeys(false))){
                    if(signChange.getCommandFromSign(loc).contains("join")
                            ||signChange.getCommandFromSign(loc).contains("spectate") ){
                        Sign sign = (Sign) loc.getWorld().getBlockAt(loc).getState();
                        if(signChange.getCommandFromSign(loc).contains("join"))
                            sign.setLine(3, ChatColor.DARK_GRAY + "<" +ChatColor.GREEN+"join"+ChatColor.DARK_GRAY + ">");
                        sign.setLine(1,ChatColor.DARK_GRAY+"Players: "+a.arenaPlayer.size());
                        sign.update();
                    }
                }
                Arena.clearEntities(a);
            }
            p.teleport(Smash.mainLobby);
            p.setMaxHealth(20);
            p.setHealthScale(20);
            p.setHealth(20);
            p.setExp(0);
            p.setLevel(0);
            p.getInventory().clear();
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }else if(Arena.arenaOfPlayer(p,false) != null){
            Arena a = Arena.arenaOfPlayer(p,false);
            a.arenaPlayer.remove(p);
            p.teleport(Smash.mainLobby);
            p.setMaxHealth(20);
            p.setHealthScale(20);
            p.setHealth(20);
            p.setExp(0);
            p.setLevel(0);
            p.getInventory().clear();
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
        if(p.getGameMode() == GameMode.SPECTATOR){
            p.teleport(Smash.mainLobby);
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

}

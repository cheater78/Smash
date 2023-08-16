package com.cheater78.smash.Game.Systems;

import com.cheater78.smash.Smash;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class DoubleJumpHandler implements Listener {

    public static final int doubleJumpCooldownTime = 7*20;
    public static final boolean rechargeInAir = false;
    public static final double lookAtBoost = 0.8;
    public static final double upBoost = 1;

    private static Map<UUID, Integer> dJumpCooldown;
    private static final float dJReady = (1.f - 0.0001f);
    private static final BukkitTask cooldownHandler = new BukkitRunnable(){
        @Override
        public void run(){
            for (UUID id : dJumpCooldown.keySet())
                if(dJumpCooldown.get(id) > 0 && (rechargeInAir || ((Entity)Bukkit.getServer().getPlayer(id)).isOnGround()))
                    dJumpCooldown.put(id, dJumpCooldown.get(id)-1);
                else if(dJumpCooldown.get(id) == 0)
                    Bukkit.getServer().getPlayer(id).setAllowFlight(true);
        }
    }.runTaskTimer(Smash.plugin, 0, 1);

    public static void addPlayer(Player p){
        dJumpCooldown.put(p.getUniqueId(), 0);
        updatePlayer(p);
    }

    public static void addPlayers(ArrayList<Player> players){
        for(Player p : players)
            addPlayer(p);
    }

    public static void remPlayer(Player p){
        dJumpCooldown.remove(p.getUniqueId());
        updatePlayer(p);
    }

    public static void remPlayers(ArrayList<Player> players){
        for(Player p : players)
            remPlayer(p);
    }

    private static void updatePlayer(Player p){
        if(dJumpCooldown.containsKey(p.getUniqueId())) {
            float relRechargeProg = dJReady - ((float) dJumpCooldown.get(p.getUniqueId()) / (float) doubleJumpCooldownTime);
            p.setExp(relRechargeProg);

            if (relRechargeProg == dJReady) {
                p.setAllowFlight(true); return;
            }
        }
        p.setAllowFlight(false);
    }

    @EventHandler
    public void onFlightAttemp(PlayerToggleFlightEvent e){
        Player p = e.getPlayer();
        if(!PlayerManager.isInGame(p)) return;
        if(!dJumpCooldown.containsKey(p.getUniqueId())) return;
        if(dJumpCooldown.get(p.getUniqueId()) > 0) return;

        p.setVelocity(p.getEyeLocation().getDirection().multiply(lookAtBoost).setY(upBoost));
        updatePlayer(p);
        p.setFlying(false);
        e.setCancelled(true);
    }
}

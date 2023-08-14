package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Game.Systems.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class RocketLauncher extends Item {

    private final float mapDmg = 3;
    private final float speed = 3;

    public RocketLauncher() {
        super(ChatColor.GOLD + "RocketLauncher", Material.DIAMOND_PICKAXE);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!PlayerManager.isInGame(p)) return;
        if (!p.getInventory().getItemInMainHand().isSimilar(this.get(false))) return;

        Fireball fire = p.getWorld().spawn(p.getEyeLocation(), Fireball.class);
        fire.setShooter(p);
        fire.setYield(mapDmg);
        fire.setVelocity(fire.getVelocity().multiply(speed));
        p.getInventory().setItemInMainHand(null);
        e.setCancelled(true);

        p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5f, 1);
    }
}

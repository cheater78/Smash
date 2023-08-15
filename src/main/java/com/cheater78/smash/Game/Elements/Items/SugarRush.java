package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Systems.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SugarRush extends Item {

    private final int sugarRushAmp = 5;
    private final int sugarRushDur = 20*5;

    public SugarRush() {
        super(ChatColor.WHITE + "SugarRush", Material.SUGAR);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!PlayerManager.isInGame(p)) return;
        if (!p.getInventory().getItemInMainHand().isSimilar(this.get(false))) return;

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,sugarRushDur, sugarRushAmp,false,false,true));
        p.getInventory().setItemInMainHand(null);
        e.setCancelled(true);
    }
}

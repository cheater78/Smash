package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Game.Systems.PlayerManager;
import com.cheater78.smash.Smash;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GodApple extends Item {

    private final double gappleHealPerc = 0.2;
    private final int gappleInvinceTime = 7*20;
    private final int gappleInvinceSlow = 1;

    public GodApple() {
        super(ChatColor.GOLD + "GodApple", Material.ENCHANTED_GOLDEN_APPLE);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!PlayerManager.isInGame(p)) return;
        if (!p.getInventory().getItemInMainHand().isSimilar(this.get(false))) return;
        SmashGame game = PlayerManager.getGame(p);

        Firework firework = p.getWorld().spawn(p.getEyeLocation(), Firework.class);
        FireworkMeta fMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.RED).build();
        fMeta.addEffect(effect);
        firework.setFireworkMeta(fMeta);
        firework.detonate();

        if(p.getLevel() <= (int)(game.getMaxExp()*gappleHealPerc))
            p.setLevel(0);
        else
            p.setLevel(p.getLevel()-(int)(game.getMaxExp()*gappleHealPerc));

        p.setInvulnerable(true);
        p.setGlowing(true);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,gappleInvinceTime, gappleInvinceSlow,false,false,true));
        p.sendTitle(" ", ChatColor.GOLD + "<Invulnerable>", 5, 20*6,15);

        p.getInventory().setItemInMainHand(null);
        e.setCancelled(true);
        Smash.sceduler.scheduleSyncDelayedTask(Smash.plugin, () -> {
            p.setInvulnerable(false);
            p.setGlowing(false);
            p.resetTitle();
        }, gappleInvinceTime);
    }
}

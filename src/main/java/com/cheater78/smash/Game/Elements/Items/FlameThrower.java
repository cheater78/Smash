package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Game.Systems.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class FlameThrower extends Item {

    private final double speed = 3;

    public FlameThrower() {
        super(ChatColor.DARK_RED + "FlameFlower", Material.POPPY);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!PlayerManager.isInGame(p)) return;
        SmashGame game = PlayerManager.getGame(p);
        if (!p.getInventory().getItemInMainHand().isSimilar(this.get(false))) return;
        ItemStack i = p.getInventory().getItemInMainHand();

        SmallFireball fire = p.getWorld().spawn(p.getEyeLocation(), SmallFireball.class);
        fire.setShooter(p);
        fire.setVelocity(fire.getVelocity().multiply(speed));
        if (i.getAmount() == 1)
            p.getInventory().setItemInMainHand(null);
        else {
            i.setAmount(i.getAmount() - 1);
            p.getInventory().setItemInMainHand(i);
        }

        e.setCancelled(true);

        p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5f, 1);
    }
}

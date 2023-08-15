package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Systems.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FlappyRocket extends Item {

    private final int flappyRocketAmount = 5;
    private final double flappyRocketUpBoost = 1.5;
    private final double flappyRocketLookBoost = 1;

    public FlappyRocket() {
        super(ChatColor.AQUA + "FlappyRocket",          Material.FIREWORK_ROCKET);
        setPickupAmount(flappyRocketAmount);
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!PlayerManager.isInGame(p)) return;
        if (!p.getInventory().getItemInMainHand().isSimilar(this.get(false))) return;

        p.setVelocity(p.getEyeLocation().getDirection().multiply(flappyRocketLookBoost).add(new Vector(0,(float) flappyRocketUpBoost,0)));
        ItemStack i = p.getInventory().getItemInMainHand();
        i.setAmount(i.getAmount()-1);
        p.getInventory().setItemInMainHand(i);
        e.setCancelled(true);
    }


}

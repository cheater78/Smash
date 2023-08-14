package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Config.GameConfig;
import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Systems.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

public class GrapplingHook extends Item {

    public GrapplingHook() {
        super(ChatColor.AQUA + "GrapplingHook", Material.FISHING_ROD);
    }

    @EventHandler
    public void onFishing(PlayerFishEvent e){
        if(e.getState() != PlayerFishEvent.State.REEL_IN) return;
        Player p = e.getPlayer();
        if(!PlayerManager.isInGame(p)) return;

        p.setVelocity(e.getHook().getLocation().toVector().subtract(p.getLocation().toVector()).multiply(GameConfig.grapplingHookMul));
        p.getInventory().setItemInMainHand(null);
        //TODO: maybe bad?!
        p.playSound(p.getEyeLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.5f, 0.0f);
    }
}

package com.cheater78.smash.Game.Elements.Items;

import com.cheater78.smash.Config.GameConfig;
import com.cheater78.smash.Game.Elements.Item;
import com.cheater78.smash.Game.Systems.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Smasher extends Item {
    public Smasher() {
        super(ChatColor.GOLD + "Smasher", Material.GOLDEN_AXE);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        if(!(e.getDamager() instanceof Player)) return;
        if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;
        Player p = (Player) e.getEntity();
        Player sp = ((Player) e.getDamager());
        if(!sp.getInventory().getItemInMainHand().isSimilar(get(false))) return;
        if(!PlayerManager.isInGame(p)) return;

        p.giveExpLevels(GameConfig.smasherDmg);
        sp.getInventory().setItemInMainHand(null);
        e.setDamage(0);
    }

}

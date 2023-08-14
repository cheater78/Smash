package com.cheater78.smash.Game.Systems;

import com.cheater78.smash.Config.GameConfig;
import com.cheater78.smash.Game.Elements.Items;
import com.cheater78.smash.Game.SmashGame;
import com.cheater78.smash.Serialize.Serializable.Location;
import com.cheater78.smash.Smash;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageHandler implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if(!PlayerManager.isInGame(p)) return;
        SmashGame game = PlayerManager.getGame(p);


        if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            if(e.getDamager() instanceof Player){
                Player sp = ((Player) e.getDamager());
                if( sp.getInventory().getItemInMainHand().isSimilar(Items.smasher.get(false)) ){
                    p.giveExpLevels(GameConfig.smasherDmg);
                    sp.getInventory().setItemInMainHand(null);
                }else p.giveExpLevels((int) (e.getDamage()*GameConfig.playerDmgMul));
            }else p.giveExpLevels((int) (e.getDamage()* GameConfig.entityDmgMul));

        //TODO: Explosion range
        if(e.getDamager().getType() == EntityType.PLAYER)
            p.setVelocity(((Player) e.getDamager()).getEyeLocation().getDirection().multiply((p.getLevel()*GameConfig.playerKnockbackMul)));
        else if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
            p.setVelocity(e.getDamager().getLocation().getDirection().multiply(p.getLevel()*GameConfig.entityExplosionKnockbackMul));
        else if(e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
            p.setVelocity(p.getLocation().subtract(e.getDamager().getLocation()).toVector().normalize().multiply(p.getLevel()*GameConfig.blockExplosionKnockbackMul));
        else p.setVelocity(p.getVelocity().multiply(-(p.getLevel()*GameConfig.entityKnockbackMul)));

        if(e.getDamager().getType() == EntityType.FIREWORK)
            e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if(!PlayerManager.isInGame(p)) return;
        SmashGame game = PlayerManager.getGame(p);



        if(e.getCause()== EntityDamageEvent.DamageCause.FALL)
            p.giveExpLevels((int) (e.getDamage()*GameConfig.fallDmgMul));
        else if(e.getCause()== EntityDamageEvent.DamageCause.FIRE)
            p.giveExpLevels((int) (e.getDamage()*GameConfig.fireDmgMul));
        else if(e.getCause()== EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
            p.giveExpLevels((int) (e.getDamage()*GameConfig.entityExplosionDmgMul));


        if(p.getLevel() >= game.getMaxExp())
            game.onDeath(p);

        e.setDamage(0);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e){
        Block b = e.getBlock();
        for (SmashGame a: Smash.games.values()){
            if(a.isGameActive()){
                Location loc = new Location(b.getLocation());
                if(loc.isInRegion(a.getArena().getPos1(), a.getArena().getPos2())){
                    e.setYield(0);
                }
            }
        }
    }


}

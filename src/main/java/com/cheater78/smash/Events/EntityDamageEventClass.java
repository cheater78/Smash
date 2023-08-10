package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Items.Items;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageEventClass implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();

            if(Arena.arenaOfPlayer(p, true) != null) {
                Arena a = Arena.arenaOfPlayer(p, true);

                if(e.getCause()== EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause()== EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK){
                    if(e.getDamager() instanceof Player){
                        Player sp = ((Player) e.getDamager());
                        if( sp.getInventory().getItemInMainHand().isSimilar(Items.smasher()) ){
                            p.giveExpLevels(60);
                            sp.getInventory().setItemInMainHand(null);
                        }else{
                            p.giveExpLevels((int) (e.getDamage()*7));
                        }
                    }else{
                        p.giveExpLevels((int) (e.getDamage()*4));
                    }
                }

                if(e.getDamager().getType() == EntityType.PLAYER){
                    p.setVelocity(((Player) e.getDamager()).getEyeLocation().getDirection().multiply((p.getLevel()/12)));
                }else if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION){
                    p.setVelocity(p.getLocation().getDirection().multiply((p.getLevel()/5)));
                }else {
                    p.setVelocity(p.getVelocity().multiply(-(p.getLevel()/16)));
                }

                if(e.getDamager().getType() == EntityType.FIREWORK){
                    e.setCancelled(true);
                }

            }

        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();

            if(Arena.arenaOfPlayer(p, true) != null){
                Arena a = Arena.arenaOfPlayer(p,true);

                if(e.getCause()== EntityDamageEvent.DamageCause.FALL){
                    p.giveExpLevels((int) (e.getDamage()*0.8));
                }else if(e.getCause()== EntityDamageEvent.DamageCause.FIRE){
                    p.giveExpLevels((int) (e.getDamage()*5));
                }else if(e.getCause()== EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
                    p.giveExpLevels((int) (e.getDamage()*3));
                }else if(!(e.getCause()== EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause()== EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)){
                    p.giveExpLevels((int) (e.getDamage()*1));
                }

                if(p.getLevel() >= a.maxExp){
                    Arena.kill(p);
                }
                e.setDamage(0);
            }
            if(Arena.arenaOfPlayer(p, false) != null){
                Arena a = Arena.arenaOfPlayer(p,false);


                e.setDamage(0);
            }


        }
    }


}

package com.cheater78.smash.Game.Systems;

import com.cheater78.smash.Events.signChange;
import com.cheater78.smash.Smash;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SignManager implements Listener {






    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if(e.getClickedBlock() != null){
            if(signTypes.contains(e.getClickedBlock().getType()) && p.hasPermission("smash.signs.use")){
                if(signChange.getCommandFromSign(e.getClickedBlock().getLocation()) != null && Smash.useSigns){
                    p.performCommand(signChange.getCommandFromSign(e.getClickedBlock().getLocation()));
                }else if((((Sign) e.getClickedBlock().getState()).getLine(0).contains("smash")
                        || ((Sign) e.getClickedBlock().getState()).getLine(0).contains("Smash"))
                        && ((Sign) e.getClickedBlock().getState()).getLine(0).contains("[")
                        && ((Sign) e.getClickedBlock().getState()).getLine(0).contains("]")){
                    if(Smash.useSigns){
                        p.sendMessage(ChatColor.RED + "If u tried to use a Smash related sign, replace the Sign. This one is not registered!");
                    }else {
                        p.sendMessage(ChatColor.RED + "If u tried to use a Smash related sign, Signs are turned off in the Config!");
                    }
                }
            }
        }


    }
}

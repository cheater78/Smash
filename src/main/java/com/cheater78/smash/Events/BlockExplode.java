package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Commands.smash;
import com.cheater78.smash.Smash;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplode implements Listener {

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e){
        Block b = e.getBlock();

        for (Arena a: Smash.arenas){
            if(a.gameActive){
                if(smash.isInRect(b.getLocation(),a.minLoc,a.maxLoc)){
                    e.setYield(0);
                }
            }
        }


    }

}

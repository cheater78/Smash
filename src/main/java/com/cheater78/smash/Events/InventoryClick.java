package com.cheater78.smash.Events;

import com.cheater78.smash.Arena;
import com.cheater78.smash.Conversion.LocationString;
import com.cheater78.smash.Items.Items;
import com.cheater78.smash.Smash;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class InventoryClick implements Listener {

    public static Plugin plugin;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        if(e.getView().getTitle().equalsIgnoreCase("Smash Match Settings")){
            e.setCancelled(true);
            Player p = (Player) e.getView().getPlayer();
            Inventory inv = e.getView().getTopInventory();

            if(e.getCurrentItem() != null){
                ItemStack i = e.getCurrentItem();
                if(Arena.arenaOfPlayer(p,false) != null){
                    Arena a = Arena.arenaOfPlayer(p,false);
                    ItemMeta imeta = i.getItemMeta();

                    if(i.getType().equals(Material.BARRIER)){
                        p.closeInventory();
                    }else if(i.getType().equals(Material.RED_DYE)){
                        if(e.getAction() == InventoryAction.PICKUP_ALL){
                            if(a.lives+1 >=11) a.lives = 1;
                            else  a.lives += 1;
                        }else if (e.getAction() == InventoryAction.PICKUP_HALF){
                            if(a.lives-1 <= 0) a.lives = 10;
                            else a.lives -= 1;
                        }else return;
                        if(imeta == null) return;
                        imeta.setDisplayName(ChatColor.RED + "Lives: " + ChatColor.GREEN + a.lives);
                        i.setItemMeta(imeta);
                    }else if(i.getType().equals(Material.EXPERIENCE_BOTTLE)){
                        if(e.getAction() == InventoryAction.PICKUP_ALL){
                            if(a.maxExp+10 >=5000) a.maxExp = 10;
                            else  a.maxExp += 10;
                        }else if (e.getAction() == InventoryAction.PICKUP_HALF){
                            if(a.maxExp-10 <= 10) a.maxExp = 5000;
                            else a.maxExp -= 10;
                        }else return;
                        if(imeta == null) return;
                        imeta.setDisplayName(ChatColor.GREEN + "MaxDamage: " + ChatColor.YELLOW + a.maxExp);
                        i.setItemMeta(imeta);
                    }else if(i.getType().equals(Material.DIAMOND_PICKAXE)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[0] = false;
                        }else {

                            a.enaItems[0] = true;
                        }
                    }else if(i.getType().equals(Material.POPPY)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[1] = false;
                        }else {

                            a.enaItems[1] = true;
                        }
                    }else if(i.getType().equals(Material.FISHING_ROD)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[2] = false;
                        }else {

                            a.enaItems[2] = true;
                        }
                    }else if(i.getType().equals(Material.GOLDEN_AXE)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[3] = false;
                        }else {

                            a.enaItems[3] = true;
                        }
                    }else if(i.getType().equals(Material.FIREWORK_STAR)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[4] = false;
                        }else {

                            a.enaItems[4] = true;
                        }
                    }else if(i.getType().equals(Material.TNT)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[5] = false;
                        }else {

                            a.enaItems[5] = true;
                        }
                    }else if(i.getType().equals(Material.RED_MUSHROOM)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[6] = false;
                        }else {

                            a.enaItems[6] = true;
                        }
                    }else if(i.getType().equals(Material.WHITE_DYE)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[7] = false;
                        }else {

                            a.enaItems[7] = true;
                        }
                    }else if(i.getType().equals(Material.SUGAR)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[8] = false;
                        }else {

                            a.enaItems[8] = true;
                        }
                    }else if(i.getType().equals(Material.GOLDEN_APPLE)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[9] = false;
                        }else {

                            a.enaItems[9] = true;
                        }
                    }else if(i.getType().equals(Material.CREEPER_SPAWN_EGG)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[10] = false;
                        }else {

                            a.enaItems[10] = true;
                        }
                    }else if(i.getType().equals(Material.FIREWORK_ROCKET)){
                        if(i.containsEnchantment(Enchantment.DURABILITY)){

                            a.enaItems[11] = false;
                        }else {

                            a.enaItems[11] = true;
                        }
                    }else return;
                    a.settingsChanged = true;
                }
            }
        }else {
            Player p = (Player) e.getView().getPlayer();
            if(e.getCurrentItem() != null){
                ItemStack i = e.getCurrentItem();
                if(Arena.arenaOfPlayer(p,false) != null) {
                    Arena a = Arena.arenaOfPlayer(p, false);
                    Inventory inv = e.getView().getTopInventory();

                    e.setCancelled(true);
                }
            }
        }
    }

    public static void updateSettingsAndScoreboardAndSigns(){

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Arena a: Smash.arenas){
                if(!a.arenaPlayer.isEmpty()){
                    if(a.gameActive){
                        if(a.playerCountChanged){
                            if(Smash.useScoreboards){
                                ScoreboardManager mngr = Bukkit.getScoreboardManager();
                                Scoreboard scb = mngr.getNewScoreboard();
                                Objective obj = scb.registerNewObjective(a.name,"dummy", ChatColor.GOLD + "Smash");
                                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                                int items = 0;
                                for(boolean b: a.enaItems){
                                    if(b) items++;
                                }
                                int plalive = 0;
                                for(Player p: a.arenaPlayer){
                                    if(p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE)
                                        plalive++;
                                }
                                Score l1 = obj.getScore("]" + ChatColor.DARK_PURPLE + " ");                                                   l1.setScore(7);
                                Score l2 = obj.getScore("]" + ChatColor.GREEN + "Lives: " + ChatColor.RED + (int)a.lives);                    l2.setScore(6);
                                Score l3 = obj.getScore("]" + ChatColor.GREEN + "MaxDmg:  " + ChatColor.RED + (int)a.maxExp);                 l3.setScore(5);
                                Score l4 = obj.getScore("]" + ChatColor.GREEN + "Items: " + ChatColor.RED + items);                           l4.setScore(4);
                                Score l5 = obj.getScore("]" + ChatColor.YELLOW + " ");                                                        l5.setScore(3);
                                Score l6 = obj.getScore("]" + ChatColor.GREEN + "Alive: " + ChatColor.RED + plalive);                         l6.setScore(2);
                                Score l7 = obj.getScore("]" + ChatColor.GREEN + " ");                                                         l7.setScore(1);

                                Objective obj1 = scb.registerNewObjective(a.name+"dmg","level", "Dmg");
                                obj1.setDisplaySlot(DisplaySlot.BELOW_NAME);

                                Objective obj2 = scb.registerNewObjective(a.name+"hp","health", "HP");
                                obj2.setDisplaySlot(DisplaySlot.PLAYER_LIST);

                                for (Player p:a.arenaPlayer){
                                    p.setScoreboard(scb);
                                }
                            }
                            if(Smash.useSigns){
                                FileConfiguration signConfig = signChange.getSignConfig();
                                for (Location loc: LocationString.KeystoLocs(signConfig.getKeys(false))){
                                    if(signChange.getCommandFromSign(loc).contains("join")
                                            ||(signChange.getCommandFromSign(loc).contains("spectate"))){
                                        Sign sign = (Sign) loc.getWorld().getBlockAt(loc).getState();
                                        int plalive = 0;
                                        for(Player p: a.arenaPlayer){
                                            if(p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE)
                                                plalive++;
                                        }
                                        sign.setLine(1,ChatColor.DARK_GRAY+"Alive: "+ChatColor.RED+plalive);
                                        sign.update();
                                        int finalPlalive = plalive;
                                        Smash.sced.scheduleSyncDelayedTask(plugin, () -> {
                                            sign.setLine(1,ChatColor.DARK_GRAY+"Alive: "+ChatColor.DARK_GRAY+ finalPlalive);
                                            if(signChange.getCommandFromSign(loc).contains("join"))
                                                sign.setLine(3,ChatColor.DARK_GRAY + "<" +ChatColor.RED+"running"+ChatColor.DARK_GRAY + ">");
                                            sign.update();
                                        },7);
                                    }
                                }
                            }
                        }
                        a.playerCountChanged = false;
                    }else {
                        if(a.settingsChanged){
                            for(Player p:a.arenaPlayer){
                                if(p.getOpenInventory().getTitle().equalsIgnoreCase("Smash Match Settings")){
                                    Inventory inv = p.getOpenInventory().getTopInventory();
                                    ArrayList<ItemStack> guiItems = Items.getItemsUnEnch();

                                    for(ItemStack is: guiItems){
                                        if(a.enaItems[guiItems.indexOf(is)]){
                                            is.addUnsafeEnchantment(Enchantment.DURABILITY,10);
                                        }
                                        if(guiItems.indexOf(is) <= 6){
                                            inv.setItem(28+guiItems.indexOf(is), is);
                                        }else if (guiItems.indexOf(is) <= 13){
                                            inv.setItem(37+guiItems.indexOf(is)-7, is);
                                        }
                                    }
                                    ItemStack lives = new ItemStack(Material.RED_DYE);
                                    ItemMeta mlives = lives.getItemMeta();
                                    mlives.setDisplayName(ChatColor.RED + "Lives: " + ChatColor.GREEN + String.valueOf(a.lives));
                                    lives.setItemMeta(mlives);
                                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE);
                                    ItemMeta mexp = exp.getItemMeta();
                                    mexp.setDisplayName(ChatColor.GREEN + "MaxDamage: " + ChatColor.YELLOW + String.valueOf(a.maxExp));
                                    exp.setItemMeta(mexp);

                                    inv.setItem(11, lives);
                                    inv.setItem(15, exp);
                                }
                            }
                            //Implemented Scorboard to Save Repeating Task
                            if(Smash.useScoreboards){
                                ScoreboardManager mngr = Bukkit.getScoreboardManager();
                                Scoreboard scb = mngr.getNewScoreboard();
                                Objective obj = scb.registerNewObjective(a.name,"dummy", ChatColor.GOLD + "Smash");
                                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                                int items = 0;
                                for(boolean b: a.enaItems){
                                    if(b) items++;
                                }

                                Score l1 = obj.getScore("]" + ChatColor.DARK_PURPLE + " ");                                                   l1.setScore(7);
                                Score l2 = obj.getScore("]" + ChatColor.GREEN + "Lives: " + ChatColor.RED + (int)a.lives);                    l2.setScore(6);
                                Score l3 = obj.getScore("]" + ChatColor.GREEN + "MaxDmg:  " + ChatColor.RED + (int)a.maxExp);                 l3.setScore(5);
                                Score l4 = obj.getScore("]" + ChatColor.GREEN + "Items: " + ChatColor.RED + items);                           l4.setScore(4);
                                Score l5 = obj.getScore("]" + ChatColor.YELLOW + " ");                                                        l5.setScore(3);
                                Score l6 = obj.getScore("]" + ChatColor.GREEN + "Players: " + ChatColor.RED + a.arenaPlayer.size());          l6.setScore(2);
                                Score l7 = obj.getScore("]" + ChatColor.GREEN + " ");                                                         l7.setScore(1);


                                for (Player p:a.arenaPlayer){
                                    p.setScoreboard(scb);
                                }
                            }
                            a.settingsChanged = false;
                        }
                        if(a.playerCountChanged){
                            FileConfiguration signConfig = signChange.getSignConfig();
                            for (Location loc: LocationString.KeystoLocs(signConfig.getKeys(false))){
                                if(signChange.getCommandFromSign(loc).contains("join")
                                        ||signChange.getCommandFromSign(loc).contains("spectate") ){
                                    Sign sign = (Sign) loc.getWorld().getBlockAt(loc).getState();
                                    if(signChange.getCommandFromSign(loc).contains("join"))
                                        sign.setLine(3,ChatColor.DARK_GRAY + "<" +ChatColor.GREEN+"join"+ChatColor.DARK_GRAY + ">");
                                    sign.setLine(1,ChatColor.DARK_GRAY+"Players: "+a.arenaPlayer.size());
                                    sign.update();
                                }
                            }
                            a.playerCountChanged =false;
                        }
                    }


                }
            }
        }, 0, 1);

    }

}

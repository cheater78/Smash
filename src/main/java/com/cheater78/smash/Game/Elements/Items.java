

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemsDep {

    // Items
    public static Item rocketLauncher  = new Item(     ChatColor.GOLD + "RocketLauncher",        Material.DIAMOND_PICKAXE);
    public static Item flameFlower     = new Item(     ChatColor.DARK_RED + "FlameFlower",       Material.POPPY);
    public static Item fishingRod      = new Item(     ChatColor.AQUA + "GrapplingHook",         Material.FISHING_ROD);
    public static Item smasher         = new Item(     ChatColor.GOLD + "Smasher",               Material.GOLDEN_AXE);
    public static Item gravityGrenade  = new Item(     ChatColor.GRAY + "GravityGrenade",        Material.FIREWORK_STAR);
    public static Item tnt             = new Item(     ChatColor.RED + "TNT",                    Material.TNT);
    public static Item healShroom      = new Item(     ChatColor.DARK_RED +       "H"
                                                            + ChatColor.RED +           "e"
                                                            + ChatColor.GOLD +          "a"
                                                            + ChatColor.YELLOW +        "l"
                                                            + ChatColor.GREEN+          "S"
                                                            + ChatColor.DARK_AQUA +     "h"
                                                            + ChatColor.BLUE +          "r"
                                                            + ChatColor.LIGHT_PURPLE +  "o"
                                                            + ChatColor.DARK_PURPLE +   "o"
                                                            + ChatColor.DARK_RED +      "m",            Material.RED_MUSHROOM);
    public static Item savePlat        = new Item(     ChatColor.WHITE + "SavingCloud",          Material.WHITE_DYE);
    //TODO: ...
    public static Item sugarRush       = new Item(     ChatColor.WHITE + "SugarRush",            Material.SUGAR);
    public static Item opGapple        = new Item(     ChatColor.GOLD + "GodApple",              Material.ENCHANTED_GOLDEN_APPLE);
    public static Item spawnEgg        = new Item(     ChatColor.DARK_PURPLE + "MobSpawnEGG",    Material.CREEPER_SPAWN_EGG);
    public static Item flappyRocket    = new Item(     ChatColor.AQUA + "FlappyRocket",          Material.FIREWORK_ROCKET);


    public static List<Item> getAll(){
        List<Item> all = new ArrayList<>();
        all.add(rocketLauncher);
        all.add(flameFlower);
        all.add(fishingRod);
        all.add(smasher);
        all.add(gravityGrenade);
        all.add(tnt);
        all.add(healShroom);
        all.add(savePlat);
        all.add(sugarRush);
        all.add(opGapple);
        all.add(spawnEgg);
        all.add(flappyRocket);
        return all;
    }

    public static List<ItemStack> getAll(boolean enchanted){
        List<ItemStack> all = new ArrayList<>();
        for(Item i : getAll())
            all.add(i.get(enchanted));
        return all;
    }



}

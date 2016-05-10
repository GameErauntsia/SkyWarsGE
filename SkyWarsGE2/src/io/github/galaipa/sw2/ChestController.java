
package io.github.galaipa.sw2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.google.common.collect.Lists;
import org.bukkit.Material;




public class ChestController {

    private final List<ChestItem> chestItemList = Lists.newArrayList();
    private final Random random = new Random();
    
    private List<Integer> randomLoc = new ArrayList<>();
    List<String> items = new ArrayList();
    public SkyWarsGE2 main;
    public ChestController(SkyWarsGE2 main) {
    	this.main = main;
        load();
        for (int i = 0; i < 27; i++) {
        	randomLoc.add(i);
        }
    }

    public void load() {
        chestItemList.clear();
        chestItemList.add(new ChestItem(new ItemStack(Material.WOOD, 32), 60));
        chestItemList.add(new ChestItem(new ItemStack(Material.COBBLESTONE, 10), 80));
        chestItemList.add(new ChestItem(new ItemStack(Material.COBBLESTONE, 12), 75));
        chestItemList.add(new ChestItem(new ItemStack(Material.SNOW_BALL, 10), 30));
        chestItemList.add(new ChestItem(new ItemStack(Material.EGG, 2), 50));
        chestItemList.add(new ChestItem(new ItemStack(Material.LAVA_BUCKET, 1), 50));
        chestItemList.add(new ChestItem(new ItemStack(Material.WATER_BUCKET, 1), 30));
        chestItemList.add(new ChestItem(new ItemStack(Material.APPLE, 1), 60));
        chestItemList.add(new ChestItem(new ItemStack(Material.GOLDEN_APPLE, 1), 20));
        chestItemList.add(new ChestItem(new ItemStack(Material.MUSHROOM_SOUP, 1), 60));
        chestItemList.add(new ChestItem(new ItemStack(Material.COOKED_FISH, 2), 75));
        chestItemList.add(new ChestItem(new ItemStack(Material.BOW, 1), 25));
        chestItemList.add(new ChestItem(new ItemStack(Material.ARROW, 15), 50));
        chestItemList.add(new ChestItem(new ItemStack(Material.ARROW, 2), 25));
        chestItemList.add(new ChestItem(new ItemStack(Material.STONE_SWORD, 1), 50));
        chestItemList.add(new ChestItem(new ItemStack(Material.WOOD_SWORD, 1), 55));
        chestItemList.add(new ChestItem(new ItemStack(Material.STONE_SWORD, 1), 25));
        chestItemList.add(new ChestItem(new ItemStack(Material.STONE_AXE, 1), 50));
        chestItemList.add(new ChestItem(new ItemStack(Material.IRON_SWORD, 1), 45));
        chestItemList.add(new ChestItem(new ItemStack(Material.IRON_SWORD, 1), 25));
        chestItemList.add(new ChestItem(new ItemStack(Material.IRON_HELMET, 1), 25));
        chestItemList.add(new ChestItem(new ItemStack(Material.IRON_CHESTPLATE, 1), 20));
        chestItemList.add(new ChestItem(new ItemStack(Material.IRON_LEGGINGS, 1), 20));
        chestItemList.add(new ChestItem(new ItemStack(Material.IRON_BOOTS, 1), 20));
        chestItemList.add(new ChestItem(new ItemStack(Material.LEATHER_HELMET, 1), 45));
        chestItemList.add(new ChestItem(new ItemStack(Material.LEATHER_CHESTPLATE, 1), 40));
        chestItemList.add(new ChestItem(new ItemStack(Material.LEATHER_LEGGINGS, 1), 40));
        chestItemList.add(new ChestItem(new ItemStack(Material.LEATHER_BOOTS, 1), 40));
        chestItemList.add(new ChestItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), 30));
        chestItemList.add(new ChestItem(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), 30));
    }

    public void KutxaBete(Chest chest) {
	Inventory inventory = chest.getBlockInventory();
 	inventory.clear();
        int added = 0;
        Collections.shuffle(randomLoc);
        for (ChestItem chestItem : chestItemList) {
             if (random.nextInt(100) + 1 <= chestItem.getChance()) {
                 inventory.setItem(randomLoc.get(added), chestItem.getItem());
                 if (added++ >= inventory.getSize()-9) {
                     System.out.println(chest);
                     break;
                 }
             }
         }
    }

    public class ChestItem {

        private ItemStack item;
        private int chance;

        public ChestItem(ItemStack item, int chance) {
            this.item = item;
            this.chance = chance;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getChance() {
            return chance;
        }
    }

}

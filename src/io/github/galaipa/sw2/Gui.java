
package io.github.galaipa.sw2;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Gui implements Listener{
    public SkyWarsGE2 plugin;
    public Gui(SkyWarsGE2 instance) {
            plugin = instance;
        }
    public static ItemStack item(Material material, int id, int amount,String name, String lore){
            ItemStack b = new ItemStack(material, amount, (short) id);
            ItemMeta metaB = b.getItemMeta();                          
            metaB.setDisplayName(name);
            ArrayList <String> gui = new ArrayList<>();
            gui.add(lore);
            metaB.setLore(gui);
            b.setItemMeta(metaB);
            return b;
    }
    public static Inventory joinGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"SkyWars: Aukeratu kit-a");
    public  static void setGui() {
        joinGUI.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu kit-a",""));
        joinGUI.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu kit-a",""));
        joinGUI.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu kit-a",""));
        joinGUI.setItem(3,item(Material.STONE_SWORD,0,1,ChatColor.RED  + "Noob","Harrizko erramintak, egurrezko ezpata eta 5 bloke "));
        joinGUI.setItem(4,item(Material.BRICK,14,1,ChatColor.RED + "Eraikitzailea","Burdinazko pikotxa eta 25 bloke"));
        joinGUI.setItem(5,item(Material.LEATHER_CHESTPLATE,0,1,ChatColor.RED  + "Zalduna","Larruzko armadura"));
        joinGUI.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu kit-a",""));
        joinGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu kit-a",""));
        joinGUI.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu kit-a",""));
    }
    public static void openGui(Player p){
        setGui();
        p.openInventory(joinGUI);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked(); 
        if (event.getInventory().getName().equals(joinGUI.getName())) {
            if(event.getCurrentItem().getItemMeta() != null ){
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Noob")){
                    player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                    player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
                    player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
                    player.getInventory().addItem(new ItemStack(Material.STONE_SPADE));
                    player.getInventory().addItem(new ItemStack(Material.STONE,5));
                    player.closeInventory();
                    plugin.join(player);
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED  + "Eraikitzailea")){
                    player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
                    player.getInventory().addItem(new ItemStack(Material.BRICK,25));
                    player.closeInventory();
                    plugin.join(player);
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED  + "Zalduna")){
                    player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET));
                    player.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
                    player.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS));
                    player.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS));
                    player.closeInventory();
                    plugin.join(player);
                }
                    
            }
        }else{
            
        }
    }
}

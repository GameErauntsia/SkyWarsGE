package io.github.galaipa.sw2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class SignListener implements Listener {
        public SkyWarsGE2 plugin;
        public SignListener(SkyWarsGE2 instance) {
            plugin = instance;
        }
 
 @EventHandler
public void SignClick(PlayerInteractEvent e) {
Player p = e.getPlayer();
    if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
        if (e.getClickedBlock().getState() instanceof Sign) { 
            Sign sign = (Sign) e.getClickedBlock().getState();
            if(sign.getLine(0).equalsIgnoreCase("[SkyWars]")) {
                if(sign.getLine(2).equals("Sartu")) {
                    p.openInventory(sartuGUI);
                }
            }
        }
    }
 }
public static Inventory sartuGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"SkyWars");
public  static void setJoinGui() {
    sartuGUI.setItem(0,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
    sartuGUI.setItem(1,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
    sartuGUI.setItem(2,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
    sartuGUI.setItem(3,item(Material.BOW,0,1,ChatColor.BLUE + "Jolastu"));
    sartuGUI.setItem(4,item(Material.EMERALD,0,1,ChatColor.BLUE + "Denda"));
   // joinGUI.setItem(4,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
    sartuGUI.setItem(5,item(Material.PUMPKIN,1,1,ChatColor.BLUE + "Ikuslea"));
    sartuGUI.setItem(6,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
    sartuGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
    sartuGUI.setItem(8,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Game Erauntsia"));
}
public static ItemStack item(Material material, int id, int amount,String name){
        ItemStack b = new ItemStack(material, amount, (short) id);
        ItemMeta metaB = b.getItemMeta();                          
        metaB.setDisplayName(name);
        metaB.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        b.setItemMeta(metaB);
        return b;
}

 }


package io.github.galaipa.sw2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Gui implements Listener{
    public SkyWarsGE2 plugin;
    public Gui(SkyWarsGE2 instance) {
            plugin = instance;
        }
    public static final Map<Player, String> kits = new HashMap<>();
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
                    if(kits.get(player) != null){
                        kits.remove(player);
                    }
                    kits.put(player, "Noob");
                    player.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN + "Noob kit-a aukeratu duzu");
                    player.closeInventory();
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED  + "Eraikitzailea")){
                    if(kits.get(player) != null){
                        kits.remove(player);
                    }
                    kits.put(player, "Eraikitzailea");
                    player.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN + "Eraikitzaile kit-a aukeratu duzu");
                    player.closeInventory();
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED  + "Zalduna")){
                    if(kits.get(player) != null){
                        kits.remove(player);
                    }
                    kits.put(player, "Zalduna");
                    player.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN + "Zaldun kit-a aukeratu duzu");
                    player.closeInventory();
                }
                    
            }
        }else{
            
        }
    }
    public static void giveKit(Player player){
        player.getInventory().clear();
        if(kits.get(player) != null){
        String kit = kits.get(player);
        switch(kit){
            case "Zalduna":
                    player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET));
                    player.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
                    player.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS));
                    player.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS));
                    break;
            case "Eraikitzailea":
                    player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
                    player.getInventory().addItem(new ItemStack(Material.BRICK,25));
                    break;
            case "Noob":
                    player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                    player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
                    player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
                    player.getInventory().addItem(new ItemStack(Material.STONE_SPADE));
                    player.getInventory().addItem(new ItemStack(Material.STONE,5));
                    break;
            default:
        }
        }
    }
    public static void maingui(Player p){
        Inventory inv = p.getInventory();
        inv.clear();
        inv.setItem(0,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.GREEN + "SkyWars Game Erauntsia",""));
        inv.setItem(1,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.GREEN + "SkyWars Game Erauntsia",""));
        inv.setItem(2,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.GREEN + "SkyWars Game Erauntsia",""));
        inv.setItem(3,item(Material.STONE_SWORD,0,1,ChatColor.GREEN + "Kit-a aukeratu",""));
        inv.setItem(4,item(Material.STAINED_CLAY,14,1,ChatColor.YELLOW + "Jokoa hasteko bozkatu",""));
        inv.setItem(5,item(Material.BARRIER,0,1,ChatColor.RED  + "Jokotik irten",""));
        inv.setItem(6,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.GREEN + "SkyWars Game Erauntsia",""));
        inv.setItem(7,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.GREEN + "SkyWars Game Erauntsia",""));
        inv.setItem(8,item(Material.STAINED_GLASS_PANE,13,1,ChatColor.GREEN + "SkyWars Game Erauntsia",""));
    }
    public void bozkatu(Player p){
        plugin.bozkaKopurua++;
        p.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.YELLOW + "Jokoa hasteko bozkatu duzu. Jokalarien %60ak bozkatzean hasiko da");
        
        if(plugin.Jokalariak.size() == 1){
            
        }else if(plugin.bozketa){
            
        }
        else if(plugin.bozkaKopurua *100 / plugin.Jokalariak.size() > 60){
            plugin.bozketa = true;
            plugin.hasiera();
        }
    }
          @EventHandler
          public void onInventoryClick2(PlayerInteractEvent event){
              if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK ){
                  Player p = event.getPlayer();
                  if(plugin.Jokalariak.contains(p)){
                  if(p.getItemInHand() != null && p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().hasDisplayName()){
                      if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN  +"Kit-a aukeratu")){
                          openGui(p);
                      }else if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW  +"Jokoa hasteko bozkatu")){
                          event.setCancelled(true);
                          p.getInventory().setItem(4,item(Material.STAINED_CLAY,5,1,ChatColor.GREEN + "Jokoa hasteko bozkatu duzu",""));
                          bozkatu(p);
                      }else if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED  +"Jokotik irten")){
                          event.setCancelled(true);
                          plugin.resetPlayer(p);
                          p.teleport(plugin.lobby);
                          p.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.RED + "Jokotik irten zara");
                      }
              }
                  }
              }
              
          }
}

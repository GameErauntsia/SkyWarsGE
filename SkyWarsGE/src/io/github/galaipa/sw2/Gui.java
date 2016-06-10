
package io.github.galaipa.sw2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
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
            metaB.setLore(Arrays.asList(lore.split("/")));
            metaB.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            b.setItemMeta(metaB);
            return b;
    }
    public static Inventory joinGUI = Bukkit.createInventory(null, 9, ChatColor.RED +"SkyWars: Aukeratu kit-a");
    public void openGui(Player p) {
        joinGUI.clear();
        if(plugin.Jokalariak.contains(p)){
            joinGUI.addItem(item(Material.STONE_SWORD,0,1,ChatColor.GREEN  + "*Noob","-harrizko erramintak/-egurrezko ezpata/-5 bloke "));
            joinGUI.addItem(item(Material.BRICK,14,1,ChatColor.GREEN + "*Eraikitzailea","-burdinazko pikotxa/-25 bloke"));
            joinGUI.addItem(item(Material.LEATHER_CHESTPLATE,0,1,ChatColor.GREEN  + "*Zalduna","-larruzko armadura"));
            joinGUI.setItem(7,item(Material.STAINED_GLASS_PANE,15,1,ChatColor.WHITE + "Aukeratu kit-a",""));
        }
        joinGUI.setItem(8,item(Material.EMERALD,15,1,ChatColor.YELLOW + "Puntuak","Zure puntuak: " + plugin.playerPoints.getAPI().look(p.getUniqueId())));
        if(p.hasPermission("sw.kit.troll")){
            joinGUI.addItem(item(Material.REDSTONE,0,1,ChatColor.GREEN  + "*Troll","-Diamantezko armadura osoa"));
        }else{
            joinGUI.addItem(item(Material.REDSTONE,0,1,ChatColor.RED  + "*Troll","-Diamantezko armadura osoa/" + ChatColor.YELLOW + "Prezioa (partida bakarra): 500 puntu"));
        }
        if(p.hasPermission("sw.kit.arrautza")){
            joinGUI.addItem(item(Material.EGG,0,1,ChatColor.GREEN  + "*Arrautza","-32 arrautza"));
        }else{
            joinGUI.addItem(item(Material.EGG,0,1,ChatColor.RED  + "*Arrautza","-32 arrautza)/" + ChatColor.YELLOW + "Prezioa (partida bakarra): 100 puntu"));
        }
        if(p.hasPermission("sw.kit.arkua")){
            joinGUI.addItem(item(Material.BOW,0,1,ChatColor.GREEN  + "*Arkua","-arkua/-10 gezi"));
        }else{
            joinGUI.addItem(item(Material.BOW,0,1,ChatColor.RED  + "*Arkua","-arkua/-10 gezi/" + ChatColor.YELLOW + "Prezioa (partida bakarra): 200 puntu"));
        }
        if(p.hasPermission("sw.kit.ezpata")){
            joinGUI.addItem(item(Material.IRON_SWORD,0,1,ChatColor.GREEN  + "*Ezpata","-burdinazko ezpata"));
        }else{
            joinGUI.addItem(item(Material.IRON_SWORD,0,1,ChatColor.RED  + "*Ezpata","-burdinazko ezpata/" + ChatColor.YELLOW + "Prezioa (partida bakarra): 200 puntu"));
        }
        p.openInventory(joinGUI);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked(); 
        if (event.getInventory().getName().equals(joinGUI.getName())) {
            if(event.getCurrentItem().getItemMeta() != null ){
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                if(clicked.getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "*")){
                    if(kits.get(player) != null){
                        kits.remove(player);
                    }
                    if(!plugin.Jokalariak.contains(player)){
                         player.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN +"Dagoeneko badaukazu!");
                        return;
                    }
                    String kit = ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).replace("*", "");
                    kits.put(player, kit);
                    player.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN + kit + " kit-a aukeratu duzu");
                    player.closeInventory();
                } else if(clicked.getItemMeta().getDisplayName().startsWith(ChatColor.RED + "*")){
                    String kit = ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).replace("*", "");
                    Integer prezioa;
                    switch(kit){
                        case "Troll":
                            prezioa = 500;
                            break;
                        case "Arrautza":
                            prezioa = 100;
                            break;
                        case "Ezpata":
                            prezioa = 200;
                            break;
                        case "Arkua":
                            prezioa = 200;
                            break;
                        default:
                            prezioa = 0;
                    }
                    if(!plugin.Jokalariak.contains(player)){
                        prezioa = prezioa*10;
                    }
                    if(plugin.playerPoints.getAPI().take(player.getUniqueId(), prezioa)) {
                        player.sendMessage(ChatColor.GREEN + "[SkyWars] " + kit + " kit-a erosi duzu. Prezioa: " + prezioa);
                        if(!plugin.Jokalariak.contains(player)){
                            plugin.perms.playerAdd(player, "sw.kit." + kit);
                        }else{
                            kits.put(player, kit);
                            player.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN + kit + " kit-a aukeratu duzu");
                        }
                    }else{
                        player.sendMessage(ChatColor.GREEN + "[SkyWars]" + ChatColor.RED + " Ez daukazu nahikoa dirurik. Prezioa: " + prezioa);
                    }
                    player.closeInventory();
                }
                    
            }
        }else if (event.getInventory().getName().equals(SignListener.sartuGUI.getName())) {
            if(event.getCurrentItem().getItemMeta() != null){
                ItemStack clicked = event.getCurrentItem(); 
                event.setCancelled(true);
                if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Ikuslea")){
                        player.closeInventory();
                        if(plugin.inGame){
                            player.teleport(plugin.spawn);
                            plugin.Ikusleak.add(player);
                            player.setScoreboard(plugin.board);
                            player.setGameMode(GameMode.SPECTATOR);
                        }else{
                            player.sendMessage(ChatColor.GREEN +"[SkyWars] " +ChatColor.RED + "Ez da inor jolasten ari");
                        }
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Jolastu")){
                    plugin.join(player);
                }else if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Denda")){
                    player.closeInventory();
                    openGui(player);
                }
            }
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
            case "Troll":
                    player.getInventory().addItem(new ItemStack(Material.REDSTONE_TORCH_OFF,10));
                    player.getInventory().addItem(new ItemStack(Material.PISTON_STICKY_BASE,2));
                    player.getInventory().addItem(new ItemStack(Material.REDSTONE,20));
                    player.getInventory().addItem(new ItemStack(Material.TNT,3));
                    player.getInventory().addItem(new ItemStack(Material.LEVER));
                    break;
            case "Arrautza":
                    player.getInventory().addItem(new ItemStack(Material.EGG,32));
                    break;
            case "Arkua":
                    player.getInventory().addItem(new ItemStack(Material.BOW,1));
                    player.getInventory().addItem(new ItemStack(Material.ARROW,10));
                    break;
            case "Ezpata":
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD,1));
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

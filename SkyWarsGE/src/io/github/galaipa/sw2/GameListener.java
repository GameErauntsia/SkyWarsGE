 
package io.github.galaipa.sw2;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.server.v1_9_R2.PacketPlayInClientCommand;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


public class GameListener implements Listener{
    public SkyWarsGE2 plugin;
    HashMap<String, Team> map = new HashMap<>();
    public static ArrayList <Player> exPlayers = new ArrayList<>();
    public static Player p2 = null;
    public GameListener(SkyWarsGE2 instance) {
        plugin = instance;
    }
          @EventHandler
          public void PlayerCommand(PlayerCommandPreprocessEvent event) {
                  Player p = event.getPlayer();
                  if(plugin.Jokalariak.contains(p)){
                      if(p.hasPermission("sw.admin")){
                          return;
                      }
                      else if(event.getMessage().toLowerCase().startsWith("/skywars")){
                        }
                      else if(event.getMessage().toLowerCase().startsWith("/sw")){
                        }
                      else{
                       event.setCancelled(true);
                       p.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + "Ezin duzu komandorik erabili jolasten zaudenean");
                      }

                  }
          }
          @EventHandler
          public void PlayerKill(PlayerDeathEvent e) {
              Player killed = e.getEntity();
              if(plugin.Jokalariak.contains(killed)){
                  if(e.getEntity().getKiller() != null){
                    if(e.getEntity().getKiller() instanceof Player){
                        Player killer = e.getEntity().getKiller();
                        e.setDeathMessage("");
                        killed.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + killer.getName() + "-(e)k hil zaitu");
                        killer.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.GREEN + killed.getName() + " hil duzu");
                        plugin.Broadcast(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + killer.getName() + "-(e)k " + killed.getName() + " hil du");
                        killer.getWorld().playSound(killer.getLocation(),Sound.BLOCK_NOTE_BASEDRUM, 10, 1);
                  }
                  }else{
                      e.setDeathMessage("");
                      killed.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + "Hil egin zara");
                      plugin.Broadcast(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + killed.getName() + " hil egin da");
                  }
                  plugin.playerPoints.getAPI().give(killed.getUniqueId(), 5);
                  killed.getPlayer().sendMessage(ChatColor.GREEN + "Zorionak! parte hartzeagatik 5 puntu irabazi dituzu");
                  plugin.resetPlayer(killed);
              }
          }
        public static void respawnPlayer(Player player) {
        /*  if (paramPlayer.isDead()){
              ((CraftServer)Bukkit.getServer()).getHandle().moveToWorld(((CraftPlayer)paramPlayer).getHandle(), 0, false);
          }*/
            if(player.isDead()){
               PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
              ((CraftPlayer)player).getHandle().playerConnection.a(packet);
            }
        }
          @EventHandler (priority = EventPriority.HIGH)
          public void teleport(PlayerRespawnEvent e){
                  Player killed = e.getPlayer();
                  if(exPlayers.contains(killed)){
                      e.setRespawnLocation(plugin.spawn);
                      killed.setGameMode(GameMode.SPECTATOR);
                      killed.setScoreboard(plugin.board);
                  }else if(e.getPlayer().equals(p2)){
                      e.setRespawnLocation(plugin.lobby);
                      p2 = null;
                  }
          }
          @EventHandler
          public void Protection(BlockBreakEvent event) {
              if (!plugin.inGame){
                  if(plugin.Jokalariak.contains(event.getPlayer())){
                          event.setCancelled(true);
                  }
              }
          }
          @EventHandler
          public void Kutxa(BlockPlaceEvent event) {
              if(plugin.config && event.getPlayer().isOp()){
                  if(event.getBlock().getType().equals(Material.CHEST)){
                      plugin.SaveKutxa(plugin.arena, event.getBlock().getLocation(), Integer.toString(plugin.kutxak));
                      plugin.kutxak++;
                      event.getPlayer().sendMessage("Kutxa ondo ezarri duzu");
                  }
              }
          }
       /*   @EventHandler
          public void PlayerHit(EntityDamageByEntityEvent e) {
            Entity damager = e.getDamager(); 
            if (damager instanceof Player) {
                Player player = (Player) damager;
                if(plugin.Jokalariak.contains(damager)){
                Entity damaged = e.getEntity();
                if (damaged instanceof Player) {
                    Player player2 = (Player) damaged;
                    if(plugin.getTeam(player) == plugin.getTeam(player2)){
                        e.setCancelled(true);
                    }
            }
            }
            }
          }*/
          @EventHandler
        public void onFallEvent(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        if (e.getCause() == DamageCause.FALL) {
                if(plugin.Jokalariak.contains(p)){
                    e.setCancelled(true);
                }
}
        }
}

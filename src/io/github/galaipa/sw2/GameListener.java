
package io.github.galaipa.sw2;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


public class GameListener implements Listener{
        public SkyWarsGE2 plugin;
        HashMap<String, Team> map = new HashMap<>();
        ArrayList exPlayers;
        public GameListener(SkyWarsGE2 instance) {
            plugin = instance;
        }
              @EventHandler
              public void PlayerCommand(PlayerCommandPreprocessEvent event) {
                  if(plugin.inGame == true){
                      Player p = event.getPlayer();
                      if(plugin.Jokalariak.contains(p)){
                              if(event.getMessage().toLowerCase().startsWith("/skywars")){
                                }
                              else if(event.getMessage().toLowerCase().startsWith("/sw")){
                                }
                              else{
                               event.setCancelled(true);
                               p.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + "Ezin duzu komandorik erabili jolasten zaudenean");
                              }
                          
                      }
                  }
              }
              @EventHandler
              public void PlayerKill(PlayerDeathEvent e) {
                  Player killed = e.getEntity().getPlayer();
                  if(plugin.Jokalariak.contains(killed)){
                        plugin.Jokalariak.remove(killed);
                        Team t = plugin.getTeam(killed);
                        plugin.teams.remove(t);
                        plugin.taldeKopurua--;
                        plugin.jokalariKopurua.setScore(plugin.Jokalariak.size());
                           for(Player p : plugin.Jokalariak){
                                p.setScoreboard(plugin.board); 
                        }
                        exPlayers.add(killed);
                      if(e.getEntity().getKiller() instanceof Player){
                        Player killer = e.getEntity().getKiller();
                        e.setDeathMessage("");
                        killed.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + killer.getName() + "-(e)k hil zaitu");
                        killer.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.GREEN + killed.getName() + " hil duzu");
                        plugin.Broadcast(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + killer.getName() + "-(e)k " + killed.getName() + " hil du");
                        killer.getWorld().playSound(killer.getLocation(),Sound.NOTE_STICKS, 10, 1);
                      }else{
                          e.setDeathMessage("");
                          killed.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + "Hil egin zara");
                          plugin.Broadcast(ChatColor.GREEN +"[SkyWars] " + ChatColor.RED + killed.getName() + " hil egin da");
                      }
                        if(plugin.taldeKopurua == 1){
                                for(Player p : plugin.Jokalariak){
                                    p.teleport(plugin.spawn);
                                    p.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.YELLOW +"Zorionak! SkyWars irabazi duzu");
                                }
                            plugin.amaiera();
                        }
                  }
              }
              @EventHandler (priority = EventPriority.HIGH)
              public void teleport(PlayerRespawnEvent e){
                  Player killed = e.getPlayer();
                  if(exPlayers != null){
                      if(exPlayers.contains(killed)){
                          e.setRespawnLocation(plugin.spawn);
                          exPlayers.remove(killed);
                      }
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

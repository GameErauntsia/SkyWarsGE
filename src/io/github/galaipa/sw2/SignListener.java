package io.github.galaipa.sw2;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


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
                        plugin.join(p);
                    }
                }
            }
        }
     }
     
     }

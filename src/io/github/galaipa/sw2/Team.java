
package io.github.galaipa.sw2;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Team {
    private int id;
    private Location spawnpoint;
    public Player Jokalaria;
    public Team(int n) {
	id = n;
	}

    public int getID(){
        return id;
    }
    public Location getSpawnPoint(){
        return spawnpoint;
    }
    public void setSpawnPoint(Location l){
        spawnpoint = l;
    }
    public void addPlayer(Player p){
        Jokalaria = p;
        }
    public void removePlayer(Player p){
            Jokalaria = null;
        }
    public void addPlayers(Player p,Player p2){
        Jokalaria = p;
    }
    public Player getPlayer(){
        return Jokalaria;
        }
    public String getPlayerString(){
        String p = Jokalaria.getName();
        String jokalariak = p;
        return jokalariak;
        }
    public Boolean checkPlayer(Player p){
        Player pa = Jokalaria;
        if(p == pa ){
            return true;
        }
        else{
            return false;
        }
        }
    }
    


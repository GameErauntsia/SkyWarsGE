
package io.github.galaipa.sw2;

import java.util.ArrayList;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.cyberiantiger.minecraft.instantreset.InstantReset;
import org.cyberiantiger.minecraft.instantreset.InstantResetWorld;


public class SkyWarsGE2 extends JavaPlugin {
    ArrayList <Team> teams = new ArrayList<>();
    ArrayList <Player> Jokalariak = new ArrayList<>();
    Location spawn;
    Boolean inGame;
    int taldeKopurua;
    Objective objective;
    ScoreboardManager manager;
    Scoreboard board;
    Score jokalariKopurua;
     
    @Override
    public void onEnable() {
            getConfig().options().copyDefaults(true);
            saveConfig(); 
            manager = Bukkit.getScoreboardManager();
            board = manager.getNewScoreboard();
            objective = board.registerNewObjective(ChatColor.DARK_GREEN.BOLD + "SkyWars", "dummy");
            defaultValues();
            getServer().getPluginManager().registerEvents(new GameListener(this), this);
            getServer().getPluginManager().registerEvents(new Gui(this), this);
            getServer().getPluginManager().registerEvents(new SignListener(this), this);
            hookPlayerPoints();
            }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("skywarsadmin")){
                if(!p.hasPermission("sw.admin")){
                    sender.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.RED + "Ez daukazu hori egiteko baimenik");
                }else if (args[0].equalsIgnoreCase("spawnpoint")){
                    SaveSpawn(p.getLocation(),args[1]);
                    sender.sendMessage("Location: " + p.getLocation());
                }else if (args[0].equalsIgnoreCase("start")){
                    sender.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN + "Jokoa orain hasiko da");
                    hasiera();    
                }else if (args[0].equalsIgnoreCase("join")){
                   join(getServer().getPlayer(args[1]));
                }
        }else if (cmd.getName().equalsIgnoreCase("skywars")){
            if(args.length < 1){
                
            }else if (args[0].equalsIgnoreCase("join")){
                p.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.RED + "Kartela erabili jokoan sartzeko");
              //  Gui.openGui(p);
            }else if (args[0].equalsIgnoreCase("leave")){
                resetPlayer(p);
                p.teleport(spawn);
                p.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.RED + "Jokotik irten zara");
            }else if (args[0].equalsIgnoreCase("start")){
                sender.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN + "Jokoa orain hasiko da");
                hasiera();
            }
        } return true;
}
   public void SaveSpawn(Location l,String t){
        getConfig().set("Spawns." + t + ".World", l.getWorld().getName());
        getConfig().set("Spawns." + t +  ".X", l.getX());
        getConfig().set("Spawns." + t +  ".Y", l.getY());
        getConfig().set("Spawns." + t +  ".Z", l.getZ());
        saveConfig();
   }
   public void resetPlayer(Player p){
        loadLobby();
        Jokalariak.remove(p);
        Team t = getTeam(p);
        teams.remove(t);
        taldeKopurua--;
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        if(inGame){
            jokalariKopurua.setScore(Jokalariak.size());
               for(Player p2 : Jokalariak){
                    p2.setScoreboard(board); 
            }
            if(taldeKopurua == 1){
                    for(Player p2 : Jokalariak){
                        p2.teleport(spawn);
                        p2.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.YELLOW +"Zorionak! SkyWars irabazi duzu." + ChatColor.GREEN + ("(+50 puntu)"));
                        playerPoints.getAPI().give(p2.getUniqueId(), 50);
                    }
                amaiera();
        }
   }
   }
    public void amaiera(){
        defaultValues();
        InstantReset irPlugin = (InstantReset) getServer().getPluginManager().getPlugin("InstantReset");
        if (irPlugin!= null && irPlugin.isEnabled()) {
            InstantResetWorld world = irPlugin.getInstantResetWorld(getConfig().getString("Spawns.1.World"));
            if (world != null) {
                irPlugin.resetWorld(world);
            }
        }
    }
   public void loadSpawn(Team team){
                String w = getConfig().getString("Spawns." + Integer.toString(team.getID()) + ".World");
                Double x = getConfig().getDouble("Spawns." + Integer.toString(team.getID()) + ".X");
                Double y = getConfig().getDouble("Spawns." + Integer.toString(team.getID()) + ".Y");
                Double z = getConfig().getDouble("Spawns." + Integer.toString(team.getID()) + ".Z");
                Location SpawnPoint = new Location(Bukkit.getServer().getWorld(w),x,y,z);
                team.setSpawnPoint(SpawnPoint);
   }
   public void loadLobby(){
            String w = getConfig().getString("Spawns.Spawn.World");
            Double x = getConfig().getDouble("Spawns.Spawn.X");
            Double y = getConfig().getDouble("Spawns.Spawn.Y");
            Double z = getConfig().getDouble("Spawns.Spawn.Z");
            Location SpawnPoint2 = new Location(Bukkit.getServer().getWorld(w),x,y,z);
            spawn = SpawnPoint2;
}
   public void defaultValues(){
       inGame = false;
       Jokalariak.clear();
       taldeKopurua = 0;
       teams.clear();
   }
    public void allPlayers(){
        for(Team team : teams){
            Player a = team.getPlayer();
            if(!Jokalariak.contains(a)){
                Jokalariak.add(a);
            }
            }
    }
  public void Broadcast(String s){
      allPlayers();
      for(Player p : Jokalariak){
          p.sendMessage(s);
      }
  }
  
  public  void sendTitleAll(Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle){
      for(Player p : Jokalariak){
          sendTitle(p,fadeIn,stay,fadeOut,title,subtitle);
      }
  }  
public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

    PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
    connection.sendPacket(packetPlayOutTimes);

    if (subtitle != null) {
        subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
        subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
        PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
        connection.sendPacket(packetPlayOutSubTitle);
    }

    if (title != null) {
        title = title.replaceAll("%player%", player.getDisplayName());
        title = ChatColor.translateAlternateColorCodes('&', title);
        IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
        connection.sendPacket(packetPlayOutTitle);
    }
}
    public void join(Player p){
        allPlayers();
        for(Player a : Jokalariak){
            if(a == p ){
            p.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.RED + "Dagoeneko bazaude sartuta");
            return;
            }
        }
        if(inGame){
            p.sendMessage(ChatColor.GREEN +"[Sky Wars]" + ChatColor.RED + "Jokoa hasita dago dagoeneko");;
        }
        else{
                taldeKopurua++;
                Broadcast(ChatColor.GREEN +"[Sky Wars] " + ChatColor.YELLOW + p.getName() + " sartu da");
                Team team = new Team(taldeKopurua);
                teams.add(team);
                team.addPlayer(p);
                loadSpawn(team);
                p.teleport(team.getSpawnPoint());
                p.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.YELLOW + "Jokoan sartu zara");
                p.getInventory().clear();
                p.getInventory().setArmorContents(null);
                p.setGameMode(GameMode.SURVIVAL);
                allPlayers();
                return;
            }
    }
   public Team getTeam(Player p){
        for(Team t : teams){
            if(t.getPlayer() == p){
                return t;
            }
            }
        return null;
   }
   public void health(){
        BukkitRunnable task;task = new BukkitRunnable() {
            int countdown = 3;
            public void run(){
                if(countdown == 0){
                for(Player p : Jokalariak){
                    p.setHealth(p.getMaxHealth());
            }
                this.cancel();
                }
            }
        };task.runTaskTimer(this, 0L, 20L);}
   public void hasiera(){
        loadLobby();
        allPlayers();
        BukkitRunnable task;task = new BukkitRunnable() {
            int countdown = 10;
            public void run(){
                for(Player p : Jokalariak){
                    p.setLevel(countdown);
                    p.sendMessage(ChatColor.GREEN + " " + countdown);
                    p.getWorld().playSound(p.getLocation(),Sound.NOTE_STICKS, 10, 1);
                    sendTitle(p,20,40,20,Integer.toString(countdown),"");
                }
                countdown--;
                if (countdown < 0) {
                    inGame = true;
                    Broadcast(ChatColor.YELLOW + "----------------------------------------------------");
                    Broadcast(ChatColor.BOLD + "" + ChatColor.GREEN  + "                         Sky Wars Game Erauntsia ");
                    Broadcast(ChatColor.BLUE + "                                Zorte on guztiei!");
                    Broadcast(ChatColor.YELLOW + "----------------------------------------------------");
                    sendTitleAll(20,40,20,ChatColor.GREEN.toString() + "Zorte on","");

                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    jokalariKopurua = objective.getScore(ChatColor.YELLOW + "Jokalari kopurua: " );
                    jokalariKopurua.setScore(Jokalariak.size());
                    Score ge = objective.getScore(ChatColor.GREEN + "GAME ERAUNTSIA" );
                    ge.setScore(0);
                    for(Team team : teams){
                        team.getPlayer().getWorld().playSound(team.getPlayer().getLocation(),Sound.NOTE_PLING, 10, 1);
                        team.getPlayer().setScoreboard(board);
                        Block b =  team.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
                        b.setType(Material.AIR); 
                    }
                    health();
                    this.cancel();
                }
                        }
                    };
task.runTaskTimer(this, 0L, 20L);
   }
    public PlayerPoints playerPoints;
    private boolean hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        return playerPoints != null; 
    }
}

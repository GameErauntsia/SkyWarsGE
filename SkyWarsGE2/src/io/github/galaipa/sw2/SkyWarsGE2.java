
package io.github.galaipa.sw2;

import de.goldengamerzone.worldreset.WorldReset;
import static io.github.galaipa.sw2.GameListener.exPlayers;
import java.util.ArrayList;
import java.util.Random;
import net.milkbowl.vault.permission.Permission;
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
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


public class SkyWarsGE2 extends JavaPlugin {
    ArrayList <Team> teams = new ArrayList<>();
    ArrayList <Player> Jokalariak = new ArrayList<>();
    Location spawn;
    Location lobby;
    Boolean inGame, bozketa,map,config = false;
    int taldeKopurua, bozkaKopurua,kutxak;
    Objective objective;
    ScoreboardManager manager;
    Scoreboard board;
    Score jokalariKopurua; 
    String arena;
    ChestController CC;
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
            CC = new ChestController(this);
            SignListener.setJoinGui();
            hookPlayerPoints();
            setupPermissions();
            }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("skywarsadmin")){
                if(!p.hasPermission("sw.admin")){
                    sender.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.RED + "Ez daukazu hori egiteko baimenik");
                }else if (args[0].equalsIgnoreCase("spawnpoint")){
                    SaveSpawn(args[1],p.getLocation(),args[2]);
                    sender.sendMessage("Location: " + p.getLocation());
                }else if (args[0].equalsIgnoreCase("start")){
                    sender.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN + "Jokoa orain hasiko da");
                    hasiera();    
                }else if (args[0].equalsIgnoreCase("join")){
                   join(getServer().getPlayer(args[1]));
                }else if (args[0].equalsIgnoreCase("proba")){
                    amaiera();
                }else if (args[0].equalsIgnoreCase("kutxak")){
                    if(config){
                        config = false;
                        getConfig().addDefault("Kutxak."+ arena + ".zenbat", kutxak);
                        p.sendMessage("Kutxak ondo ezarri dituzu("+ kutxak +")");
                    }else{
                        config = true;
                        arena = args[1];
                        kutxak = 1;
                        p.sendMessage("Kutxak jartzen has zaitezke");
                    }
                }
        }else if (cmd.getName().equalsIgnoreCase("skywars")){
            if(args.length < 1){
                
            }else if (args[0].equalsIgnoreCase("join")){
                p.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.RED + "Kartela erabili jokoan sartzeko");
              //  Gui.openGui(p);
            }else if (args[0].equalsIgnoreCase("leave")){
                resetPlayer(p);
                p.teleport(lobby);
                p.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.RED + "Jokotik irten zara");
            }/*else if (args[0].equalsIgnoreCase("start")){
                sender.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.GREEN + "Jokoa orain hasiko da");
                if(taldeKopurua == 1){
                    sender.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.RED + "Bi jokalari behar dira gutxienez jokoa hasteko");
                }else{
                    hasiera();
                }
                
            }*/
        } return true;
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
                if(taldeKopurua == 1){
                    Random rand = new Random();
                   // int randomNum = rand.nextInt((4 - 1) + 1) + 1;
                   int randomNum = 1;
                    arena = Integer.toString(randomNum);
                    loadSpawn2(arena);
                    int zenbat = getConfig().getInt("Kutxak."+ arena + ".zenbat");
                    for (int i = 1; i <= zenbat ; i++) {
                        loadKutxa(arena,i);
                    }
                }
                loadSpawn(arena,team);
                p.teleport(team.getSpawnPoint());
                p.sendMessage(ChatColor.GREEN +"[Sky Wars] " + ChatColor.YELLOW + "Jokoan sartu zara");
                p.getInventory().clear();
                p.getInventory().setArmorContents(null);
                p.setGameMode(GameMode.SURVIVAL);
                allPlayers();
                Gui.maingui(p);
                return;
            }
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
            if(GameListener.exPlayers.contains(p)){
                GameListener.exPlayers.remove(p);
                p.teleport(lobby);
                p.setGameMode(GameMode.SURVIVAL);
            }
            if(taldeKopurua == 1){
                GameListener.p2 = p;
                GameListener.respawnPlayer(p);
                for(Player p2 : Jokalariak){
                    p2.getInventory().clear();
                    p2.getInventory().setArmorContents(null);
                    p2.teleport(lobby);
                    p2.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.YELLOW +"Zorionak! SkyWars irabazi duzu." + ChatColor.GREEN + ("(+30 puntu)"));
                    playerPoints.getAPI().give(p2.getUniqueId(), 30);
                }
                amaiera();
            }else{
                exPlayers.add(p);
                GameListener.respawnPlayer(p);
            }
            
   }
   }
   public void hasiera(){
        loadLobby();
        allPlayers();
        BukkitRunnable task;task = new BukkitRunnable() {
            int countdown = 10;
            @Override
            public void run(){
                for(Player p : Jokalariak){
                    p.setLevel(countdown);
                    //p.sendMessage(ChatColor.GREEN + " " + countdown);
                    p.getWorld().playSound(p.getLocation(),Sound.NOTE_STICKS, 10, 1);
                    sendTitle(p,20,40,20,ChatColor.YELLOW +Integer.toString(countdown),"");
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
                        Gui.giveKit(team.getPlayer());
                    }
                    health();
                    this.cancel();
                }
                        }
                    };
task.runTaskTimer(this, 0L, 20L);
   }
   public void amaiera(){
    for(Player p : GameListener.exPlayers){
        p.teleport(lobby);
        p.setGameMode(GameMode.SURVIVAL);
        p.sendMessage(ChatColor.GREEN +"[SkyWars] " + ChatColor.YELLOW +"Mila esker jolasteagatik");
    }
    defaultValues();
    WorldReset.resetWorld("SkyWars");
}
   public void loadSpawn(String arena, Team team){
        String w = getConfig().getString("Spawns."+ arena + "."  + Integer.toString(team.getID()) + ".World");
        Double x = getConfig().getDouble("Spawns." + arena + "." + Integer.toString(team.getID()) + ".X");
        Double y = getConfig().getDouble("Spawns." + arena + "." + Integer.toString(team.getID()) + ".Y");
        Double z = getConfig().getDouble("Spawns." + arena + "." + Integer.toString(team.getID()) + ".Z");
        Location SpawnPoint = new Location(Bukkit.getServer().getWorld(w),x,y,z);
        team.setSpawnPoint(SpawnPoint);
   }
   public void loadSpawn2(String arena){
        String w = getConfig().getString("Spawns."+ arena + ".0.World");
        Double x = getConfig().getDouble("Spawns." + arena + ".0.X");
        Double y = getConfig().getDouble("Spawns." + arena + ".0.Y");
        Double z = getConfig().getDouble("Spawns." + arena + ".0.Z");
        Location SpawnPoint = new Location(Bukkit.getServer().getWorld(w),x,y,z);
        spawn = SpawnPoint;
   }
   public void loadLobby(){
        String w = getConfig().getString("Spawns.Spawn.World");
        Double x = getConfig().getDouble("Spawns.Spawn.X");
        Double y = getConfig().getDouble("Spawns.Spawn.Y");
        Double z = getConfig().getDouble("Spawns.Spawn.Z");
        Location SpawnPoint2 = new Location(Bukkit.getServer().getWorld(w),x,y,z);
        lobby = SpawnPoint2;
}
   public void loadKutxa(String arena, Integer id){
        String w = getConfig().getString("Kutxak."+ arena + "."  + Integer.toString(id) + ".World");
        Double x = getConfig().getDouble("Kutxak." + arena + "." + Integer.toString(id) + ".X");
        Double y = getConfig().getDouble("Kutxak." + arena + "." + Integer.toString(id) + ".Y");
        Double z = getConfig().getDouble("Kutxak." + arena + "." + Integer.toString(id) + ".Z");
        Location Kutxa = new Location(Bukkit.getServer().getWorld(w),x,y,z);
        CC.KutxaBete((Chest)Kutxa.getBlock().getState());
   }
   public void SaveSpawn(String arena, Location l,String t){
        getConfig().set("Spawns." + arena + "." + t + ".World", l.getWorld().getName());
        getConfig().set("Spawns." + arena + "."+ t +  ".X", l.getX());
        getConfig().set("Spawns." + arena + "."+ t +  ".Y", l.getY());
        getConfig().set("Spawns." + arena + "."+ t +  ".Z", l.getZ());
        saveConfig();
   }
   public void SaveKutxa(String arena, Location l,String t){
        getConfig().set("Kutxak." + arena + "." + t + ".World", l.getWorld().getName());
        getConfig().set("Kutxak." + arena + "."+ t +  ".X", l.getX());
        getConfig().set("Kutxak." + arena + "."+ t +  ".Y", l.getY());
        getConfig().set("Kutxak." + arena + "."+ t +  ".Z", l.getZ());
        saveConfig();
   }
   public void defaultValues(){
       inGame = false;
       bozketa = false;
       Jokalariak.clear();
       bozkaKopurua = 0;
       taldeKopurua = 0;
       teams.clear();
       GameListener.exPlayers.clear();
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
    public PlayerPoints playerPoints;
    private boolean hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        return playerPoints != null; 
    }
    public Permission perms = null;
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}

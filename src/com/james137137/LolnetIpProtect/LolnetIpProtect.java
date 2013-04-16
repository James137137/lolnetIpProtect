/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.james137137.LolnetIpProtect;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

/**
 *
 * @author James
 */
public class LolnetIpProtect extends JavaPlugin {

    static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new LolnetIpProtectListener(this), this);
        log.info("LolnetIP: enabled");
        try {
    Metrics metrics = new Metrics(this);
    metrics.start();
    } catch (IOException e) {
    // Failed to submit the stats :-(
        System.out.println(e.toString());
    }
        //IpProtectedPlayerList = config.getStringList("Playerlist");
    }

    public void onDisable() {
        log.info("LolnetIpProtect: disabled");
    }

    public void removeConfigFile(CommandSender sender)
    {
        
        try{

    		File file = new File("plugins/"+this.getName()+"/config.yml");
 
    		if(file.delete()){
    			sender.sendMessage(file.getName() + " is deleted!");
    		}else{
    			sender.sendMessage("Delete operation is failed.");
    		}
 
    	}catch(Exception e){
 
    		e.printStackTrace();
 
    	}
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String commandName = command.getName().toLowerCase();
        String[] trimmedArgs = args;
        if (commandName.equalsIgnoreCase("lolnetIP")) {
            if ((sender.isOp()) || (sender == getServer().getConsoleSender())) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.YELLOW + "[LolnetIpProtect]: " + ChatColor.RESET + "commands: ");
                    sender.sendMessage("/lolnetIP [add,remove,list,mode,removeConfigFile]");
                } else if (args[0].equalsIgnoreCase("add")) {
                    addPlayerIP(sender, args);
                } else if (args[0].equalsIgnoreCase("list")) {
                    listPlayerIP(sender, args);
                } else if (args[0].equalsIgnoreCase("remove")) {
                    RemovePlayerIP(sender, args); 
                }  else if (args[0].equalsIgnoreCase("Mode")) {
                    ModePlayerIP(sender, args);
                } else if (args[0].equalsIgnoreCase("RemoveConfigFile")) {
                    removeConfigFile(sender);
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "[LolnetIpProtect]: " + ChatColor.RESET + "commands: ");
                    sender.sendMessage("/lolnetIP [add,remove,list,mode,removeConfigFile]");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
            }
        }
        return false;
    }

    public boolean isIpProtected(String playerName) {
        FileConfiguration config = getConfig();
        playerName = playerName.toLowerCase();
        List<String> IpProtectedPlayerList = config.getStringList("LolnetIP.Playerlist");
        for (int i = 0; i < IpProtectedPlayerList.size(); i++) {
            if (playerName.equalsIgnoreCase(IpProtectedPlayerList.get(i))) {

                return true;
            }
        }
        return false;
    }

    public boolean isCorrectIP(String playerName, String currentIP) throws UnknownHostException {

        FileConfiguration config = getConfig();
        playerName = playerName.toLowerCase();
        String IP;
        List<String> IpProtectedPlayerList = config.getStringList("LolnetIP." + playerName);

        

        for (int i = 0; i < IpProtectedPlayerList.size(); i++) {

            IP = IpProtectedPlayerList.get(i);

            if (Character.getNumericValue(IP.charAt(0)) > 9) {
                InetAddress dynAddress = java.net.InetAddress.getByName(IP);
                IP = dynAddress.getHostAddress();
            }

            if (IP.equalsIgnoreCase(currentIP)) {
                return true;

            }

        }
        return false;
    }

    public void addPlayerIP(CommandSender sender, String[] args) {

        if (args.length != 3) {
            sender.sendMessage("Error; must be /lolnetIP add username IP");
            return;
        }
        FileConfiguration config = getConfig();
        List<String> PlayerIPList;
        String playerName = args[1].toLowerCase();
        String IP = args[2];
        if (!isIpProtected(playerName)) {
            List<String> IpProtectedPlayerList = config.getStringList("LolnetIP.Playerlist");
            IpProtectedPlayerList.add(playerName);
            config.set("LolnetIP.Playerlist", IpProtectedPlayerList);
        }
        PlayerIPList = config.getStringList("LolnetIP." + playerName);

        PlayerIPList.add(IP);



        config.set("LolnetIP." + playerName, PlayerIPList);
        
        sender.sendMessage("IP: " + IP + " added for player: " + playerName);
        saveConfig();

    }

    public void RemovePlayerIP(CommandSender sender, String[] args) {
        if (args.length <= 1 && args.length > 3) {
            sender.sendMessage("Error; must be /lolnetIP remove username IP or /lolnetIP remove username");
            return;
        }
        FileConfiguration config = getConfig();
        List<String> PlayerIPList;
        String playerName = args[1].toLowerCase();
        
        if (!isIpProtected(playerName))
         {
             sender.sendMessage("player has not been added yet. please use /lolnetip add");
             return;
         }

        if (args.length == 2) {
            RemovePlayer(playerName);
            return;
        }
        String IP = args[2];
        if (isIpProtected(playerName)) {
            PlayerIPList = config.getStringList("LolnetIP." + playerName);
            for (int i = 0; i < PlayerIPList.size(); i++) {
                if (PlayerIPList.get(i).equalsIgnoreCase(IP)) {
                    PlayerIPList.remove(i);
                    sender.sendMessage("IP removed");
                }
            }
            
            if (IP.equalsIgnoreCase("all"))
                {
                    PlayerIPList.clear();
                    sender.sendMessage("All IPs removed from player: " + playerName);
                }
            if (PlayerIPList.isEmpty()) {
                RemovePlayer(playerName);
            }
            config.set("LolnetIP." + playerName, PlayerIPList);
            saveConfig();
        } else {
            sender.sendMessage("Player not found. Try /lolnetIP list for spelling");
        }
    }

    public void listPlayerIP(CommandSender sender, String[] args) {
        FileConfiguration config = getConfig();
        List<String> IpProtectedPlayerList = config.getStringList("LolnetIP.Playerlist");
        if (IpProtectedPlayerList.isEmpty()) {
            sender.sendMessage("Protected IP player List is empty");
            return;
        }
        if (args.length == 1) {


            if (IpProtectedPlayerList.isEmpty()) {
                sender.sendMessage("player is not IP Protected");
                return;
            }
            sender.sendMessage("Current Player list with IP protection:");
            String message = "";
            for (int i = 0; i < IpProtectedPlayerList.size(); i++) {
                message += IpProtectedPlayerList.get(i)+ ":" + GetPlayerMode(IpProtectedPlayerList.get(i)) + ", ";
            }
            sender.sendMessage(message);
        } else if (args.length == 2) {
            String playerName = args[1].toLowerCase();
            List<String> PlayerIPList = config.getStringList("LolnetIP." + playerName);
            sender.sendMessage(playerName + " has the following IPs");
            for (int i = 0; i < PlayerIPList.size(); i++) {
                sender.sendMessage("*" +PlayerIPList.get(i) + "*");
            }

        } else {
            sender.sendMessage(ChatColor.RED + "something is missing. Type /lolnetIP list or /lolnetIP list playername");
        }
    }

    private void RemovePlayer(String playerName) {
        FileConfiguration config = getConfig();
        List<String> IpProtectedPlayerList = config.getStringList("LolnetIP.Playerlist");
        for (int i = 0; i < IpProtectedPlayerList.size(); i++) {
            if (IpProtectedPlayerList.get(i).equalsIgnoreCase(playerName)) {
                IpProtectedPlayerList.remove(i);
                break;
            }
        }
        config.set("LolnetIP.Playerlist", IpProtectedPlayerList);
        saveConfig();
    }

    private void ModePlayerIP(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("Error; must be /lolnetIP mode username [protect,warn]");
            return;
        }
        
         FileConfiguration config = getConfig();
         String playerName = args[1].toLowerCase();
         
         if (!isIpProtected(playerName))
         {
             sender.sendMessage("player has not been added yet. please use /lolnetip add");
             return;
         }
         String Mode = args[2];
                 if (!Mode.equalsIgnoreCase("protect") && !Mode.equalsIgnoreCase("warn"))
                 {
                     sender.sendMessage("Error; must be /lolnetIP mode username [protect,warn]");
                     return;
                 }
                 
                 if (Mode.equalsIgnoreCase("protect"))
                 {
                     config.set("lolnetIP.ProtectionMode" +playerName,"Protect");
                     saveConfig();
                 }else if (Mode.equalsIgnoreCase("warn"))
                 {
                     config.set("lolnetIP.ProtectionMode" +playerName,"Warn");
                     saveConfig();
                 }
                 sender.sendMessage("Mode Changed to: " + Mode+ " for player: " + playerName);
                 
    }
    
    public String GetPlayerMode(String playerName)
    {
        String Mode;
        FileConfiguration config = getConfig();
        Mode = config.getString("lolnetIP.ProtectionMode" +playerName);
        
        if (Mode==null)
        {
            config.set("lolnetIP.ProtectionMode" +playerName,"Warn");
            Mode = "Warn";
            saveConfig();
        }
        return Mode;
    }
}

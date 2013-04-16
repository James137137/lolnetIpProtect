/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.james137137.LolnetIpProtect;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author James
 */

public class LolnetIpProtectListener implements Listener {
    static final Logger log = Logger.getLogger("Minecraft");
    private LolnetIpProtect LolnetIpProtect;
    private BukkitTask task;
    
    public LolnetIpProtectListener (LolnetIpProtect LolnetIpProtect)
    {
        this.LolnetIpProtect = LolnetIpProtect;
    }
    
    

    
    
    @EventHandler
   public void onPlayerJoin(final PlayerLoginEvent event) throws UnknownHostException
     {
         final Player player = event.getPlayer();
         final String playerName = player.getName().toLowerCase();
         final String ip = event.getAddress().toString().substring(1);
         LolnetIpProtect.getServer().getScheduler().scheduleSyncDelayedTask(LolnetIpProtect, new Runnable() {
             @Override
             public void run() {
                 
                 
                if (LolnetIpProtect.isIpProtected(playerName))
         {
             String playerMode = LolnetIpProtect.GetPlayerMode(playerName);
                     try {
                         if (!LolnetIpProtect.isCorrectIP(playerName,ip))
                         {
                             
                            log.log(Level.WARNING, "[IpProtect]: Incorect IP for player: {0} With IP: {1}", new Object[]{playerName, ip});
                             if (playerMode.equalsIgnoreCase("Protect"))
                             {
                             player.kickPlayer("Wrong IP: " + ip);
                             event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Wrong IP: " + ip);
                             } else 
                             {
                                 
                                 task = LolnetIpProtect.getServer().getScheduler().runTaskTimerAsynchronously(LolnetIpProtect, new Runnable() {
                                        @Override
                                        public void run() {
                                
                                            
                                            player.sendMessage(ChatColor.DARK_RED + "[Warning] " + ChatColor.RED + "Your IP is incorrect on your account. Please login and message a staff, else you might be ipbanned If you think this is an error please contant james137137 with your IP /mail send james137137 message"); 
                                            if (player.isOnline())
                                            {
                                                task.cancel();
                                            }
                                            
                                        }
                                    }, 120L, 600L);
                                player.sendMessage(ChatColor.DARK_RED + "[Warning] " + ChatColor.RED + "Your IP is incorrect on your account. Please login and message a staff, else you might be ipbanned If you think this is an error please contant james137137 with your IP /mail send james137137 message"); 
                                String message = playerName + " IP: " + ip +" isn't on the lolnetIP list, please make sure he saying something on /mb, else IPban and kick within 1-5min if you can't kick prepare to /kickall";
                                Player[] onlinePlayerList = Bukkit.getServer().getOnlinePlayers();
                                for (int i = 0; i < onlinePlayerList.length; i++) {
                                    if (onlinePlayerList[i].hasPermission("LolnetIpProtect.Warn"))
                                    {
                                        if (!onlinePlayerList[i].getName().equalsIgnoreCase(playerName))
                                        {
                                            onlinePlayerList[i].sendMessage(ChatColor.DARK_RED + "[Warning] " +  ChatColor.RED + message);
                                        }
                                    }
                                    
                                }
                                
                             }
                             
                         }
                     } catch (UnknownHostException ex) {
                         log.log(Level.SEVERE, null, ex);
                     }
             
         } 
                 
             }
         }, 1L);
         
     }
    
            
            
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.james137137.LolnetIpProtect.threads;

import com.james137137.LolnetIpProtect.LolnetIpProtect;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author James
 */
public class OnPlayerJoin implements Runnable {

    private Player player;
    private String playerName;
    private LolnetIpProtect lolnetIpProtect;
    private String ip;
    static final Logger log = Logger.getLogger("Minecraft");

    public OnPlayerJoin(LolnetIpProtect passMain, Player passedPlayer, String passIP) {
        this.player = passedPlayer;
        this.lolnetIpProtect = passMain;
        this.ip = passIP;
        this.playerName = passedPlayer.getName();
    }

    @Override
    public void run() {
        String playerMode = lolnetIpProtect.GetPlayerMode(playerName);
        try {
            if (!lolnetIpProtect.isCorrectIP(playerName, ip)) {

                log.log(Level.WARNING, "[IpProtect]: Incorect IP for player: {0} With IP: {1}", new Object[]{playerName, ip});
                if (playerMode.equalsIgnoreCase("Protect")) {
                    player.kickPlayer("Wrong IP: " + ip);
                }
                else if (playerMode.equalsIgnoreCase("Warn")){
                    player.sendMessage(ChatColor.DARK_RED + "[Warning] " + 
                            ChatColor.RED + "Your IP is incorrect on your account. Please login and message a staff, "
                            + "else you might be ipbanned If you think this is an error please contact your server "
                            + "administrator");
                    
                    String message = playerName + " IP: " + ip +" isn't on the lolnetIP list, please make "
                            + "sure he saying something on /mb, else IPban and kick within "
                            + "1-5min if you can't kick prepare to /kickall";
                    
                    Player[] onlinePlayerList = lolnetIpProtect.getServer().getOnlinePlayers();
                    
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
            Logger.getLogger(OnPlayerJoin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

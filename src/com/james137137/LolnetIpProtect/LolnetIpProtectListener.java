/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.james137137.LolnetIpProtect;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import com.james137137.LolnetIpProtect.threads.OnPlayerJoin;

/**
 *
 * @author James
 */
public class LolnetIpProtectListener implements Listener {

    private LolnetIpProtect LolnetIpProtect;

    public LolnetIpProtectListener(LolnetIpProtect LolnetIpProtect) {
        this.LolnetIpProtect = LolnetIpProtect;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerLoginEvent event) {



        if (LolnetIpProtect.isIpProtected(event.getPlayer().getName().toLowerCase())) {
            OnPlayerJoin oPJ = new OnPlayerJoin(LolnetIpProtect, event.getPlayer(), event.getAddress().toString().substring(1));
            Thread oPJThread = new Thread(oPJ);
            oPJThread.start();
        }

    }
}

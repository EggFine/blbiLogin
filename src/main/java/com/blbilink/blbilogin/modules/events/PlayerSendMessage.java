package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerSendMessage implements Listener {
    private BlbiLogin plugin = BlbiLogin.plugin;
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        ifCantSendMessage(e, player);
    }

    private void ifCantSendMessage(AsyncPlayerChatEvent event,Player player) {
        if(Configvar.config.getBoolean("noLoginPlayerCantSendMessage") && Configvar.noLoginPlayerList.contains(event.getPlayer().getName())){
            event.setCancelled(true);
        }
    }
}

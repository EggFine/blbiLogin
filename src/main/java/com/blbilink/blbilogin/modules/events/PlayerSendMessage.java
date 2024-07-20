package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerSendMessage implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        ifCantSendMessage(event);
    }

    private void ifCantSendMessage(AsyncPlayerChatEvent event) {
        if(Configvar.noLoginPlayerCantSendMessage){
            event.setCancelled(true);
        }
    }
}

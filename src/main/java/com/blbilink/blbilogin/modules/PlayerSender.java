package com.blbilink.blbilogin.modules;

import com.blbilink.blbilogin.BlbiLogin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class PlayerSender implements Listener {
    @EventHandler
    public static void noLoginPlayerSendTitle(PlayerJoinEvent e) {
        if(Configvar.noLoginPlayerSendActionBar || Configvar.noLoginPlayerSendTitle || Configvar.noLoginPlayerSendSubTitle || Configvar.noLoginPlayerSendMessage ||
                Configvar.noRegisterPlayerSendTitle || Configvar.noRegisterPlayerSendSubTitle || Configvar.noRegisterPlayerSendMessage || Configvar.noRegisterPlayerSendActionBar){

            Player player = e.getPlayer();

            if (Configvar.isFolia) {
                // Folia 环境下的调度
                player.getScheduler().runAtFixedRate(plugin, (task) -> {
                    if (Configvar.noLoginPlayerList.contains(player.getName())) {
                        sendPlayerMessages(player);
                    } else {
                        task.cancel();
                    }
                }, null, 1L, 60L);
            } else {
                // 非 Folia 环境下的调度
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (Configvar.noLoginPlayerList.contains(player.getName())) {
                            sendPlayerMessages(player);
                        } else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(BlbiLogin.plugin, 0L, 60L);
            }
        }
    }

    private static void sendPlayerMessages(Player player) {
        if(plugin.getSqlite().playerExists(player.getUniqueId().toString())) {
            if(Configvar.noLoginPlayerSendActionBar) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(plugin.i18n.as("noLoginPlayerSendActionBar",false, player.getName())));
            }
            if(Configvar.noLoginPlayerSendMessage) {
                player.sendMessage(plugin.i18n.as("noLoginPlayerSendMessage", true, player.getName()));
            }
            if(Configvar.noLoginPlayerSendTitle) {
                player.sendTitle(plugin.i18n.as("noLoginPlayerSendTitle", false, player.getName()), null, 0, 100, 0);
            }
            if(Configvar.noLoginPlayerSendSubTitle) {
                player.sendTitle(null, plugin.i18n.as("noLoginPlayerSendSubTitle", false, player.getName()), 0, 100, 0);
            }
            if(Configvar.noLoginPlayerSendMessage || Configvar.noLoginPlayerSendActionBar) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            }
        } else {
            if(Configvar.noRegisterPlayerSendActionBar) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(plugin.i18n.as("noRegisterPlayerSendActionBar",false, player.getName())));
            }
            if(Configvar.noRegisterPlayerSendMessage) {
                player.sendMessage(plugin.i18n.as("noRegisterPlayerSendMessage", true, player.getName()));
            }
            if(Configvar.noRegisterPlayerSendTitle) {
                player.sendTitle(plugin.i18n.as("noRegisterPlayerSendTitle", false, player.getName()), null, 0, 100, 0);
            }
            if(Configvar.noRegisterPlayerSendSubTitle) {
                player.sendTitle(null, plugin.i18n.as("noRegisterPlayerSendSubTitle", false,player.getName()), 0, 100, 0);
            }
            if(Configvar.noRegisterPlayerSendMessage || Configvar.noRegisterPlayerSendActionBar) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            }
        }
    }
}
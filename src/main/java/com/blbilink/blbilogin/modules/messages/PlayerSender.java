package com.blbilink.blbilogin.modules.messages;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.modules.Sqlite;
import com.blbilink.blbilogin.modules.events.CheckOnline;
import com.blbilink.blbilogin.modules.events.LoginAction;
import com.blbilink.blbilogin.vars.Configvar;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Timer;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class PlayerSender implements Listener {
    CheckOnline check = CheckOnline.INSTANCE;
    LoginAction login = LoginAction.INSTANCE;
    private Timer timer;
    @EventHandler
    public static void noLoginPlayerSendTitle(PlayerJoinEvent e) {
        if (Configvar.config.getBoolean("noLoginPlayerSendActionBar") ||
                Configvar.config.getBoolean("noLoginPlayerSendTitle") ||
                Configvar.config.getBoolean("noLoginPlayerSendSubTitle") ||
                Configvar.config.getBoolean("noLoginPlayerSendMessage") ||
                Configvar.config.getBoolean("noRegisterPlayerSendTitle") ||
                Configvar.config.getBoolean("noRegisterPlayerSendSubTitle") ||
                Configvar.config.getBoolean("noRegisterPlayerSendMessage") ||
                Configvar.config.getBoolean("noRegisterPlayerSendActionBar")) {

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
        if(Sqlite.getSqlite().playerExists(player.getUniqueId().toString())) {
            if(Configvar.config.getBoolean("noLoginPlayerSendActionBar")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(plugin.i18n.as("noLoginPlayerSendActionBar",false, player.getName())));
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendMessage")) {
                player.sendMessage(plugin.i18n.as("noLoginPlayerSendMessage", true, player.getName()));
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendTitle")) {
                player.sendTitle(plugin.i18n.as("noLoginPlayerSendTitle", false, player.getName()), null, 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendSubTitle")) {
                player.sendTitle(null, plugin.i18n.as("noLoginPlayerSendSubTitle", false, player.getName()), 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendMessage") || Configvar.config.getBoolean("noLoginPlayerSendActionBar")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            }
        } else {
            if(Configvar.config.getBoolean("noRegisterPlayerSendActionBar")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(plugin.i18n.as("noRegisterPlayerSendActionBar",false, player.getName())));
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendMessage")) {
                player.sendMessage(plugin.i18n.as("noRegisterPlayerSendMessage", true, player.getName()));
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendTitle")) {
                player.sendTitle(plugin.i18n.as("noRegisterPlayerSendTitle", false, player.getName()), null, 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendSubTitle")) {
                player.sendTitle(null, plugin.i18n.as("noRegisterPlayerSendSubTitle", false,player.getName()), 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendMessage") || Configvar.config.getBoolean("noRegisterPlayerSendActionBar")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            }
        }
    }
}
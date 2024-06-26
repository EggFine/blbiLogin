package com.blbilink.blbilogin.modules;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.load.Load;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class PlayerSender implements Listener {
    @EventHandler
    public static void onPlayerJoinSendTitle(PlayerJoinEvent e) {
        if(Configvar.noLoginPlayerSendActionBar || Configvar.noLoginPlayerSendTitle || Configvar.noLoginPlayerSendSubTitle || Configvar.noLoginPlayerSendMessage){
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (Configvar.noLoginPlayerList.contains(e.getPlayer().getName())) {
                        if(Configvar.noLoginPlayerSendActionBar){
                            e.getPlayer().sendActionBar(Load.getMessage("noLoginPlayerSendActionBar", "您当前§c未登录§f, 操作可能受限, 使用 /login <密码> 进行登录",e.getPlayer().getName(),false));
                        }
                        if(Configvar.noLoginPlayerSendMessage){
                            e.getPlayer().sendMessage(Load.getMessage("noLoginPlayerSendMessage", "您当前§c未登录§f, 操作可能受限, 使用 /login <密码> 进行登录",e.getPlayer().getName(),true));
                        }
                        if(Configvar.noLoginPlayerSendTitle){
                            e.getPlayer().sendTitle(Load.getMessage("noLoginPlayerSendTitle", "§l欢迎使用§fblbi§bLogin",e.getPlayer().getName(),false), null, 0, 100, 0);
                        }
                        if(Configvar.noLoginPlayerSendSubTitle){
                            e.getPlayer().sendTitle(null,Load.getMessage("noLoginPlayerSendSubTitle", "请使用 §6/login <密码>§f 登录游戏",e.getPlayer().getName(),false),0,100,0);
                        }
                        if(Configvar.noLoginPlayerSendMessage || Configvar.noLoginPlayerSendActionBar){
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(BlbiLogin.plugin, 0L, 60L);
        }

    }
}

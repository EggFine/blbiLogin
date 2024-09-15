package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import com.blbilink.blbilogin.modules.Sqlite;
import static com.blbilink.blbilogin.BlbiLogin.plugin;

public enum LoginAction {
	INSTANCE;
    public BlbiLogin plugin;
	
    public void sync(BlbiLogin plugin){
        this.plugin = plugin;
    }

    public void loginSuccess(Player player){
        Configvar.noLoginPlayerList.remove(player.getName());
        String msgLoginSuccess = plugin.i18n.as("msgLoginSuccess",true,player.getName());
        player.sendMessage(msgLoginSuccess);
        if (Configvar.config.getBoolean("successLoginSendTitle") || Configvar.config.getBoolean("successLoginSendSubTitle")){
            if(Configvar.config.getBoolean("successLoginSendTitle")){
                player.sendTitle(plugin.i18n.as("successLoginSendTitle",false ,player.getName()), null, 20, 100, 20);
            }
            if(Configvar.config.getBoolean("successLoginSendSubTitle")){
                player.sendTitle(null,plugin.i18n.as("successLoginSendSubTitle",false ,player.getName()),20,100,20);
            }
        }
        if(Configvar.canFlyingPlayerList.contains(player.getName())){
            player.setAllowFlight(true);
            Configvar.canFlyingPlayerList.remove(player.getName());
        }else{
            player.setAllowFlight(false);
        }
        player.setFlying(false);
        loginTeleport(player);
        if(Configvar.config.getBoolean("playerJoinAutoTeleportToSavedLocation_AutoBack")){
            player.getPlayer().playSound(Configvar.originalLocation.get(player.getName()), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }else{
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }

    }
    private void loginTeleport(Player player){
        if(Configvar.config.getBoolean("playerJoinAutoTeleportToSavedLocation") && Configvar.config.getBoolean("playerJoinAutoTeleportToSavedLocation_AutoBack")){
            if(plugin.foliaUtil.isFolia){
                player.getScheduler().run(plugin, task -> {
                    player.teleportAsync(Configvar.originalLocation.get(player.getName())).thenAccept(result -> {
                        if (result) {
                            Configvar.originalLocation.remove(player.getName());
                        }
                    });
                }, () -> {
                });
            }else{
                player.teleport(Configvar.originalLocation.get(player.getName()));
                Configvar.originalLocation.remove(player.getName());
            }
        }
    }
	
	public boolean isCorrect(String uuid, String password) {
		return Sqlite.getSqlite().checkPassword(uuid, password);
	}
}

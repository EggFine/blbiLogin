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
        if(Configvar.playerJoinAutoTeleportToSavedLocation && Configvar.playerJoinAutoTeleportToSavedLocation_AutoBack){
            if(Configvar.isFolia){
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
        String msgLoginSuccess = plugin.i18n.as("msgLoginSuccess",true,player.getName());
        player.sendMessage(msgLoginSuccess);
        if (Configvar.successLoginSendTitle || Configvar.successLoginSendSubTitle){
            if(Configvar.successLoginSendTitle){
                player.sendTitle(plugin.i18n.as("successLoginSendTitle",false ,player.getName()), null, 20, 100, 20);
            }
            if(Configvar.successLoginSendSubTitle){
                player.sendTitle(null,plugin.i18n.as("successLoginSendSubTitle",false ,player.getName()),20,100,20);
            }
        }
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        if(!player.isOp()){
            player.setAllowFlight(false);
        }
        player.setFlying(false);
    }
	
	public boolean isCorrect(String uuid, String password) {
		return Sqlite.getSqlite().checkPassword(uuid, password);
	}
}

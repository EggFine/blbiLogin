package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.modules.Sqlite;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class Login implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以使用此命令！");
            return true;
        }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        if (command.getName().equalsIgnoreCase("login")) {
            if (args.length != 1) {
                player.sendMessage("用法: /login <密码>");
                return true;
            }

            String password = args[0];

            if (!Sqlite.getSqlite().playerExists(uuid)) {
                player.sendMessage(plugin.i18n.as("msgPlayerNotRegister", true,player.getName()));
                return true;
            }

            if (Sqlite.getSqlite().checkPassword(uuid, password)) {
                loginSuccess(player);
                return true;
            } else {
                player.sendMessage(plugin.i18n.as("msgLoginPasswordWrong",true,player.getName()));
                return true;
            }
        }
        return false;
    }

    private void loginSuccess(Player player){
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
        if(!player.isOp()){
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
    }
}

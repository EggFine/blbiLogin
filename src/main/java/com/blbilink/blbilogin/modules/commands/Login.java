package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.modules.Configvar;
import com.blbilink.blbilogin.modules.Sqlite;
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
                Configvar.noLoginPlayerList.remove(player.getName());
                return true;
            } else {
                player.sendMessage(plugin.i18n.as("msgLoginPasswordWrong",true,player.getName()));
                return true;
            }
        }
        return false;
    }
}

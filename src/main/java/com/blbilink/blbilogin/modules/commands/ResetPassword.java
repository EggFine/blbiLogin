package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.modules.Sqlite;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class ResetPassword implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("resetpassword")) {
            if (sender instanceof Player) {
                if (args.length == 2) {
                    String password = args[0];
                    String newPassword = args[1];
                    if (Sqlite.getSqlite().checkPassword(((Player) sender).getUniqueId().toString(), password)) {
                        if (Sqlite.getSqlite().resetPassword(((Player) sender).getUniqueId().toString(), newPassword)) {
                            ((Player) sender).kickPlayer(plugin.i18n.as("kickByPasswordRested",false ,sender.getName()));
                        } else {
                            sender.sendMessage("§4密码重置失败");
                        }
                    } else {
                        sender.sendMessage(plugin.i18n.as("msgLoginPasswordWrong",true,sender.getName()));
                    }
                } else {
                    sender.sendMessage(String.format(plugin.i18n.as("msgCommandWrong",true,"/resetpassword <nowPassword> <newPassword>")));
                }
            } else {
                if (args.length == 2) {
                    String playerName = args[0];
                    String newPassword = args[1];
                    Player player = Bukkit.getPlayer(playerName);
                    if (player != null && player.isOnline()) {
                        if (Sqlite.getSqlite().resetPassword(player.getUniqueId().toString(), newPassword)) {
                            player.kickPlayer(plugin.i18n.as("kickByPasswordRested",false,sender.getName()));
                            plugin.getLogger().info(playerName + "的密码已被重置为" + newPassword);
                        } else {
                            plugin.getLogger().severe("密码重置失败");
                        }
                    } else {
                        plugin.getLogger().severe("玩家不在线");
                    }
                } else {
                    plugin.getLogger().severe(String.format(plugin.i18n.as("msgCommandWrong",false,"/resetpassword <playerName> <newPassword>")));
                }
            }
        }
        return false;
    }
}

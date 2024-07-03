package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.load.Load;
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
                    if (plugin.getSqlite().checkPassword(((Player) sender).getUniqueId().toString(), password)) {
                        if (plugin.getSqlite().resetPassword(((Player) sender).getUniqueId().toString(), newPassword)) {
                            ((Player) sender).kickPlayer(Load.getMessage("kickByPasswordRested", "§f你的密码已被重置, 请重新登录",sender.getName(),false));
                        } else {
                            sender.sendMessage("§4密码重置失败");
                        }
                    } else {
                        sender.sendMessage(Load.getMessage("msgLoginPasswordWrong", "输入的§c密码有误§f, 请检查后重试",sender.getName(),true));
                    }
                } else {
                    sender.sendMessage(String.format(Load.getMessage("msgCommandWrong","§c输入的指令有错误§f, 正确的用法是 §6%s",sender.getName(),true),"/resetpassword <nowPassword> <newPassword>"));
                }
            } else {
                if (args.length == 2) {
                    String playerName = args[0];
                    String newPassword = args[1];
                    Player player = Bukkit.getPlayer(playerName);
                    if (player != null && player.isOnline()) {
                        if (plugin.getSqlite().resetPassword(player.getUniqueId().toString(), newPassword)) {
                            player.kickPlayer(Load.getMessage("kickByPasswordRested", "§f你的密码已被重置, 请重新登录",sender.getName(),false));
                            plugin.getLogger().info(playerName + "的密码已被重置为" + newPassword);
                        } else {
                            plugin.getLogger().severe("密码重置失败");
                        }
                    } else {
                        plugin.getLogger().severe("玩家不在线");
                    }
                } else {
                    plugin.getLogger().severe(String.format(Load.getMessage("msgCommandWrong","§c输入的指令有错误§f, 正确的用法是 §6%s",null,false),"/resetpassword <playerName> <newPassword>"));
                }
            }
        }
        return false;
    }
}

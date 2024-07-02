package com.blbilink.blbilogin.modules.commands;

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
                            ((Player) sender).kickPlayer("§f密码重置成功, 请重新登录");
                        } else {
                            sender.sendMessage("§4密码重置失败");
                        }
                    } else {
                        sender.sendMessage("§4密码错误");
                    }
                } else {
                    sender.sendMessage("§4命令不正确, 你应该使用 /resetpassword <password> <newPassword>");
                }
            } else {
                if (args.length == 2) {
                    String playerName = args[0];
                    String newPassword = args[1];
                    Player player = Bukkit.getPlayer(playerName);
                    if (player != null && player.isOnline()) {
                        if (plugin.getSqlite().resetPassword(player.getUniqueId().toString(), newPassword)) {
                            player.kickPlayer("§f你的密码已被重置, 请重新登录");
                            plugin.getLogger().info(playerName + "的密码已被重置为" + newPassword);
                        } else {
                            plugin.getLogger().severe("密码重置失败");
                        }
                    } else {
                        plugin.getLogger().severe("玩家不在线");
                    }
                } else {
                    plugin.getLogger().severe("命令不正确, 你应该使用 /resetpassword <playerName> <newPassword>");
                }
            }
        }
        return false;
    }
}

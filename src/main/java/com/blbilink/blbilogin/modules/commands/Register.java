package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.load.Load;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class Register implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以使用此命令！");
            return true;
        }
        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        if (command.getName().equalsIgnoreCase("register")) {
            if (args.length == 1) {
                String password = args[0];

                if (plugin.getSqlite().playerExists(uuid)) {
                    player.sendMessage(Load.getMessage("msgAlreadyRegistered", "已经注册过啦, 无需重复注册.",player.getName(),true));
                    return true;
                }

                plugin.getSqlite().registerPlayer(uuid, player.getName(), password);
                player.sendMessage(Load.getMessage("msgRegisterSuccess", "注册成功, 欢迎新玩家 %player%.",player.getName(),true));
                return true;

            }
            player.sendMessage("用法: /register <密码>");
            return true;

        }
        return false;
    }
}

package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.load.Load;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class BlbiLoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("blbilogin")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("blbilogin.reload")) {
                    Load.loadConfig(plugin);
                    player.sendMessage(Load.getMessage("msgReloaded", "§f配置文件及语言文件的§a重载已经完成.",player.getName(),true));
                    return true;
                } else {
                    player.sendMessage(Load.getMessage("msgNoPermission", "§f你当前§c没有权限§f执行该操作.",player.getName(),true));
                    return true;
                }
            }
        }
        return false;
    }
}

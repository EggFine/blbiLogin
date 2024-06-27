package com.blbilink.blbilogin.modules;
// 导入命令注册包

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            player.sendMessage("HelloWorld");
        }else Bukkit.getLogger().warning("你不能在控制台使用这个命令");
        return false;
    }
}

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


        if (command.getName().equalsIgnoreCase("blbilogin")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if((sender instanceof Player)){
                    Player player = (Player) sender;
                    if (player.hasPermission("blbilogin.reload")) {
                        Load.loadConfig(plugin);
                        player.sendMessage(plugin.i18n.as("msgReloaded",true,player.getName()));
                        return true;
                    } else {
                        player.sendMessage(plugin.i18n.as("msgNoPermission",true,player.getName()));
                        return true;
                    }
                }else{
                    Load.loadConfig(plugin);
                    plugin.getLogger().info(plugin.i18n.as("msgReloaded",false));
                    return true;
                }

            }
        }

        return false;
    }
}

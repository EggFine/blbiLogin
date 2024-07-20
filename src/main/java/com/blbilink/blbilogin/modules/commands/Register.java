package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.modules.Sqlite;
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
            sender.sendMessage(String.format(plugin.i18n.as("msgCommandOnlyPlayer",true)));
            return true;
        }
        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        if (command.getName().equalsIgnoreCase("register")) {
            if (args.length == 1) {
                String password = args[0];

                if (Sqlite.getSqlite().playerExists(uuid)) {
                    player.sendMessage(plugin.i18n.as("msgAlreadyRegistered",true,player.getName()));
                    return true;
                }

                Sqlite.getSqlite().registerPlayer(uuid, player.getName(), password);
                player.sendMessage(plugin.i18n.as("msgRegisterSuccess",true,player.getName()));
                return true;

            }
            player.sendMessage(String.format(plugin.i18n.as("msgCommandWrong",true, "/register <password>")));
            return true;

        }
        return false;
    }
}

package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.modules.Sqlite;
import com.blbilink.blbilogin.modules.events.LoginAction;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class Login implements CommandExecutor {
    private final LoginAction login = LoginAction.INSTANCE;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();
        if(!Configvar.noLoginPlayerList.contains(sender.getName())){
            player.sendMessage(plugin.i18n.as("msgAlreadyLogin", true,player.getName()));
            return true;
        }
        if (command.getName().equalsIgnoreCase("login")) {
            if (args.length != 1) {
                player.sendMessage(String.format(plugin.i18n.as("msgCommandWrong",true,"/login <password>")));
                return true;
            }

            String password = args[0];

            if (!Sqlite.getSqlite().playerExists(uuid)) {
                player.sendMessage(plugin.i18n.as("msgPlayerNotRegister", true,player.getName()));
                return true;
            }

            if (login.isCorrect(uuid, password)) {
                login.loginSuccess(player);
                return true;
            } else {
                player.sendMessage(plugin.i18n.as("msgLoginPasswordWrong",true,player.getName()));
                return true;
            }
        }
        return false;
    }


}

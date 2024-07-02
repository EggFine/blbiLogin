package com.blbilink.blbilogin.modules.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResetPassword implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("resetpassword")){
            if(sender instanceof Player){
                if(args.length == 2){
                    String password = args[0];
                    String newPassword = args[1];
                }else{
                    sender.sendMessage("命令不正确, 你应该使用 /resetpassword <password> <newPassword>");
                }
            }
        }
        return false;
    }
}

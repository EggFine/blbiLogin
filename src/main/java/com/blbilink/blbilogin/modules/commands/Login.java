package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.load.Load;
import com.blbilink.blbilogin.modules.Configvar;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class Login implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以使用此命令！");
            return true;
        }
        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        if (command.getName().equalsIgnoreCase("login")) {
            if (args.length != 1) {
                player.sendMessage("用法: /login <密码>");
                return true;
            }

            String password = args[0];

            if (!plugin.getSqlite().playerExists(uuid)) {
                player.sendMessage(Load.getMessage("msgPlayerNotRegister", "你还没有注册, 请先使用 §6/register <password> §f进行注册.",player.getName(),true));
                return true;
            }

            if (plugin.getSqlite().checkPassword(uuid, password)) {
                String msgLoginSuccess = Load.getMessage("msgLoginSuccess", "登录成功, %player% 欢迎回来.",player.getName(),true);
                player.sendMessage(msgLoginSuccess);
                if (Configvar.successLoginSendTitle || Configvar.successLoginSendSubTitle){
                    if(Configvar.successLoginSendTitle){
                        player.sendTitle(Load.getMessage("successLoginSendTitle", "§a§l登录成功",player.getName(),false), null, 20, 100, 20);
                    }
                    if(Configvar.successLoginSendSubTitle){
                        player.sendTitle(null,Load.getMessage("successLoginSendSubTitle", "欢迎回来: §a%player%",player.getName(),false),20,100,20);
                    }
                }
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                if(!player.isOp()){
                    player.setAllowFlight(false);
                }
                player.setFlying(false);
                Configvar.noLoginPlayerList.remove(player.getName());
                return true;
            } else {
                player.sendMessage(Load.getMessage("msgLoginPasswordWrong", "输入的§c密码有误§f, 请检查后重试",player.getName(),true));
                return true;
            }
        }
        return false;
    }
}

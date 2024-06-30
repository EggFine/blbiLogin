package com.blbilink.blbilogin.modules;
// 导入命令注册包
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import com.blbilink.blbilogin.load.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class Commands implements CommandExecutor {
    public static Commands commands;
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
                plugin.noLoginPlayerList.remove(player.getName());
                return true;
            } else {
                player.sendMessage(Load.getMessage("msgLoginPasswordWrong", "输入的§c密码有误§f, 请检查后重试",player.getName(),true));
                return true;
            }
        }

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

package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.load.LoadConfig;
import com.blbilink.blbilogin.modules.Configvar;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class BlbiLoginCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (command.getName().equalsIgnoreCase("blbilogin")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                reload(sender);
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("setspawn")) {
                saveLocation(sender);
                return true;
            }
        }

        return false;
    }
    private void reload(CommandSender sender){
        if((sender instanceof Player player)){
            if (player.hasPermission("blbilogin.reload")) {
                LoadConfig.loadConfig(plugin);
                player.sendMessage(plugin.i18n.as("msgReloaded",true,player.getName()));
            } else {
                player.sendMessage(plugin.i18n.as("msgNoPermission",true,player.getName()));
            }
        }else{
            LoadConfig.loadConfig(plugin);
            plugin.getLogger().info(plugin.i18n.as("msgReloaded",false));
        }
    }
    private void saveLocation(CommandSender sender){
        if((sender instanceof Player player)){
            if (player.hasPermission("blbilogin.savelocation")) {
                Location loc = player.getLocation();
                Configvar.config.set("locationPos.world", loc.getWorld().getName());
                Configvar.config.set("locationPos.x", loc.getX());
                Configvar.config.set("locationPos.y", loc.getY());
                Configvar.config.set("locationPos.z", loc.getZ());
                Configvar.config.set("locationPos.yaw", loc.getYaw());
                Configvar.config.set("locationPos.pitch", loc.getPitch());
                player.sendMessage(plugin.i18n.as("msgSavedLocation",true,player.getName()));
                try {
                    Configvar.config.save(Configvar.configFile);
                    player.sendMessage(plugin.i18n.as("msgSavedConfigFile",true,player.getName()));
                } catch (IOException e) {
                    player.sendMessage(plugin.i18n.as("msgFailSaveConfigFile",true,player.getName()));
                    e.printStackTrace();
                }
            } else {
                player.sendMessage(plugin.i18n.as("msgNoPermission",true,player.getName()));
            }
        }else{
            plugin.getLogger().info(plugin.i18n.as("msgCommandOnlyPlayer"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // 第一级命令补全
            String[] subCommands = {"reload", "savelocation"};
            StringUtil.copyPartialMatches(args[0], Arrays.asList(subCommands), completions);
        }

        return completions;
    }
}

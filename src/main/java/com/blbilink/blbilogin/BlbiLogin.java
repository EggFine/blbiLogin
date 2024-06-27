//包本体
package com.blbilink.blbilogin;

//导入Bukkit配置文件包

import com.blbilink.blbilogin.load.Load;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
public final class BlbiLogin extends JavaPlugin implements Listener {

    public static BlbiLogin plugin;
    private final List<String> noLoginPlayerList = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);
        // Plugin startup logic
        this.getLogger().info("波比登录系统开始注入。");
        // 注册Bukkit事件监听器
        Bukkit.getPluginManager().registerEvents(this, this);

        Load load = new Load();
        load.loadConfig(this);
        load.loadLanguage(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!noLoginPlayerList.contains(e.getPlayer().getName())) {
            this.getLogger().info("检测到玩家 " + e.getPlayer().getName() + " 进入服务器, 已封锁玩家移动.");
            noLoginPlayerList.add(e.getPlayer().getName());

        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (noLoginPlayerList.contains(e.getPlayer().getName())) {
            this.getLogger().info("未登录玩家 " + e.getPlayer().getName() + "尝试移动, 已回弹.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (noLoginPlayerList.contains(e.getPlayer().getName()) || e.getAction() == Action.LEFT_CLICK_AIR) {
            this.getLogger().info("玩家 " + e.getPlayer().getName() + "成功登录, 已解除封锁.");
            noLoginPlayerList.remove(e.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerCommondSend(PlayerCommandSendEvent e) {
        if (noLoginPlayerList.contains(e.getPlayer().getName())) {

        } else {
            e.getPlayer().sendMessage("您已处于登录状态");
        }
    }
}
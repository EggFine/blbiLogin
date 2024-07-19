// 包本体
package com.blbilink.blbilogin;

import com.blbilink.blbilogin.load.LoadConfig;
import com.blbilink.blbilogin.load.LoadFunction;
import com.blbilink.blbilogin.modules.Configvar;
import org.blbilink.blbiLibrary.BlbiLibrary;
import org.blbilink.blbiLibrary.I18n;
import org.blbilink.blbiLibrary.Metrics;
import org.blbilink.blbiLibrary.utils.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class BlbiLogin extends JavaPlugin implements Listener {
    public static BlbiLogin plugin;
    public I18n i18n;

    @Override
    public void onEnable() {
        plugin = this;

        // 检查是否是 Folia 服务端核心
        Configvar.isFolia = BlbiLibrary.checkFolia(plugin, true);

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        // 打印 Logo
        getLogger().info(TextUtil.getLogo(
                "Loading...",
                "BLBILOGIN",
                "SpigotMC: https://www.spigotmc.org/resources/117672/",
                plugin,
                Arrays.asList("EggFine"),
                Arrays.asList("Mgazul")));

        getLogger().info("波比登录系统开始注入。");


        // 初始化插件
        LoadConfig.loadConfig(this);
        LoadFunction loadFunction = new LoadFunction(this);
        loadFunction.loadFunction();

        // 加载 bStats 统计
        Metrics metrics = new Metrics(this, 22490);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
    }


    @Override
    public void onDisable() {
        // 打印 Logo
        getLogger().info(TextUtil.getLogo(
                "Disabling...",
                "BLBILOGIN",
                "SpigotMC: https://www.spigotmc.org/resources/117672/",
                plugin,
                Arrays.asList("EggFine"),
                Arrays.asList("Mgazul")));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (Configvar.noLoginPlayerList.contains(e.getPlayer().getName()) && Configvar.noLoginPlayerCantMove) {
            String msgNoLoginTryMove = i18n.as("logNoLoginTryMove", false, e.getPlayer().getName());
            this.getLogger().info(msgNoLoginTryMove);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        if (Configvar.noLoginPlayerList.contains(e.getPlayer().getName()) && Configvar.noLoginPlayerCantBreak) {
            getLogger().info("未登录玩家 " + e.getPlayer().getName() + " 尝试挖掘方块" + e.getBlock().getType().name() + "已进行阻止.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {

            if (Configvar.noLoginPlayerList.contains(player.getName()) && Configvar.noLoginPlayerCantHurt) {
                EntityDamageEvent.DamageCause currentDamageCause = e.getCause();
                String damageCauseName = currentDamageCause.name();

                getLogger().info("未登录玩家 " + player.getName() + " 受到伤害 " + damageCauseName + " 已进行阻止.");
                e.setCancelled(true);
            }
        }
    }
}
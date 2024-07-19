// 包本体
package com.blbilink.blbilogin;

import com.blbilink.blbilogin.load.Load;
import com.blbilink.blbilogin.modules.Configvar;
import com.blbilink.blbilogin.modules.PlayerSender;
import com.blbilink.blbilogin.modules.Sqlite;
import com.blbilink.blbilogin.modules.commands.BlbiLoginCommand;
import com.blbilink.blbilogin.modules.commands.Login;
import com.blbilink.blbilogin.modules.commands.Register;
import com.blbilink.blbilogin.modules.commands.ResetPassword;
import org.blbilink.blbiLibrary.BlbiLibrary;
import org.blbilink.blbiLibrary.I18n;
import org.blbilink.blbiLibrary.Metrics;
import org.blbilink.blbiLibrary.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class BlbiLogin extends JavaPlugin implements Listener {
    private Sqlite sqlite;
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
        // 注册Bukkit事件监听器
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new PlayerSender(), this);
        // 初始化插件
        Load.loadConfig(this);
        // 初始化sqlite数据库
        sqlite = new Sqlite();

        Objects.requireNonNull(getCommand("blbilogin")).setExecutor(new BlbiLoginCommand());
        Objects.requireNonNull(getCommand("register")).setExecutor(new Register());
        Objects.requireNonNull(getCommand("login")).setExecutor(new Login());
        Objects.requireNonNull(getCommand("resetpassword")).setExecutor(new ResetPassword());

        Metrics metrics = new Metrics(this, 22490);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
    }

    public Sqlite getSqlite() {
        return sqlite;
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

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!Configvar.noLoginPlayerList.contains(e.getPlayer().getName())) {
            getLogger().info(i18n.as("logPlayerJoin", false, e.getPlayer().getName()));
            Configvar.noLoginPlayerList.add(e.getPlayer().getName());

            if (Configvar.noLoginPlayerParticle) {
                Player player = e.getPlayer();
                if (Configvar.isFolia) {
                    // Folia 环境
                    player.getScheduler().runAtFixedRate(plugin, task -> {
                        if (Configvar.noLoginPlayerList.contains(player.getName())) {
                            createFallingParticlesAroundPlayer(player);
                        } else {
                            task.cancel();
                        }
                    }, null, 1L, 20L);
                } else {
                    // 非 Folia 环境
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (Configvar.noLoginPlayerList.contains(player.getName())) {
                                createFallingParticlesAroundPlayer(player);
                            } else {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 20L);
                }
            }
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        }
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


    @EventHandler
    public void onPlayerCommandSend(PlayerCommandPreprocessEvent e) {
        List<String> cmds = new ArrayList<>(List.of("/login", "/l", "/reg", "/register"));
        cmds.addAll(Configvar.noLoginPlayerAllowUseCommand);
        String message = e.getMessage();
        boolean isAllowedCommand = false;

        if (Configvar.noLoginPlayerList.contains(e.getPlayer().getName())) {
            for (String s : cmds) {
                if (message.startsWith(s)) {
                    isAllowedCommand = true;
                    break; // 找到匹配的命令就退出循环
                }
            }
            if (!isAllowedCommand && Configvar.noLoginPlayerCantUseCommand) {
                e.setCancelled(true);
            }
        }
    }

    public void createFallingParticlesAroundPlayer(Player player) {
        Location playerLocation = player.getLocation();
        double radius = 1.5; // 粒子圈的半径
        int particleCount = 10; // 每圈粒子数量

        for (int i = 0; i < particleCount; i++) {
            // 计算粒子位置
            double angle = 2 * Math.PI * i / particleCount;
            double x = playerLocation.getX() + radius * Math.cos(angle);
            double y = playerLocation.getY() + 1;
            double z = playerLocation.getZ() + radius * Math.sin(angle);

            // 设置粒子位置
            Location particleLocation = new Location(playerLocation.getWorld(), x, y, z);


            // 发送粒子效果
            Particle particle = Particle.valueOf("FIREWORKS_SPARK");
            playerLocation.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0.1); // 使用Firework粒子效果
        }

    }
}
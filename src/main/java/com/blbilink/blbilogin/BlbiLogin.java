// 包本体
package com.blbilink.blbilogin;
import com.blbilink.blbilogin.load.*;
import com.blbilink.blbilogin.modules.*;
// 导入 Bukkit 配置文件包
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
// 导入数组列表包
import java.util.ArrayList;
import java.util.List;

public final class BlbiLogin extends JavaPlugin implements Listener {
    public final List<String> noLoginPlayerList = new ArrayList<>();
    private Sqlite sqlite;
    public static BlbiLogin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);
        // Plugin startup logic
        getLogger().info("波比登录系统开始注入。");
        // 注册Bukkit事件监听器
        Bukkit.getPluginManager().registerEvents(this, this);
        // 初始化插件
        Load.loadConfig(this);
        // CheckOnline.initialize(this);
        // 初始化sqlite数据库
        sqlite = new Sqlite();

        getCommand("login").setExecutor(new Commands());
        getCommand("register").setExecutor(new Commands());
        getCommand("blbilogin").setExecutor(new Commands());
    }
    public Sqlite getSqlite() {
        return sqlite;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!noLoginPlayerList.contains(e.getPlayer().getName())) {
            String msgPlayerLogin = Load.getMessage("logPlayerJoin", "检测到玩家 %player% 进入服务器, 已封锁玩家移动.",e.getPlayer().getName(),false);
            this.getLogger().info(msgPlayerLogin);
            noLoginPlayerList.add(e.getPlayer().getName());



//            Player player = e.getPlayer();
//            if(CheckOnline.isPlayerPremium(player)){
//                player.sendMessage("欢迎正版玩家！");
//            }else{
//                player.sendMessage("您正在使用离线账户游戏。");
//                noLoginPlayerList.add(e.getPlayer().getName());
//            }
//            CheckOnline.checkPremiumStatus(player).thenAccept(isPremium -> {
//                if (isPremium) {
//                    // 玩家拥有正版账户，可以在这里执行相应的操作
//                    player.sendMessage("欢迎正版玩家！");
//                } else {
//                    // 玩家没有正版账户
//                    player.sendMessage("您正在使用离线账户游戏。");
//                    noLoginPlayerList.add(e.getPlayer().getName());
//                }
//            });

        }
        if(Configvar.noLoginPlayerSendActionBar || Configvar.noLoginPlayerSendTitle || Configvar.noLoginPlayerSendSubTitle || Configvar.noLoginPlayerSendMessage){
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (noLoginPlayerList.contains(e.getPlayer().getName())) {
                        if(Configvar.noLoginPlayerSendActionBar){
                            e.getPlayer().sendActionBar(Load.getMessage("noLoginPlayerSendActionBar", "您当前§c未登录§f, 操作可能受限, 使用 /login <密码> 进行登录",e.getPlayer().getName(),false));
                        }
                        if(Configvar.noLoginPlayerSendMessage){
                            e.getPlayer().sendMessage(Load.getMessage("noLoginPlayerSendMessage", "您当前§c未登录§f, 操作可能受限, 使用 /login <密码> 进行登录",e.getPlayer().getName(),true));
                        }
                        if(Configvar.noLoginPlayerSendTitle){
                            e.getPlayer().sendTitle(Load.getMessage("noLoginPlayerSendTitle", "§l欢迎使用§fblbi§bLogin",e.getPlayer().getName(),false), null, 0, 100, 0);
                        }
                        if(Configvar.noLoginPlayerSendSubTitle){
                            e.getPlayer().sendTitle(null,Load.getMessage("noLoginPlayerSendSubTitle", "请使用 §6/login <密码>§f 登录游戏",e.getPlayer().getName(),false),0,100,0);
                        }
                        if(Configvar.noLoginPlayerSendMessage || Configvar.noLoginPlayerSendActionBar){
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(this, 0L, 60L);
        }
        if(Configvar.noLoginPlayerParticle){
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (noLoginPlayerList.contains(e.getPlayer().getName())) {
                        createFallingParticlesAroundPlayer(e.getPlayer());
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(this, 0L, 20L);
        }
        e.getPlayer().setAllowFlight(true);
        e.getPlayer().setFlying(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (noLoginPlayerList.contains(e.getPlayer().getName()) && Configvar.noLoginPlayerCantMove) {
            String msgNoLoginTryMove = Load.getMessage("logNoLoginTryMove", "未登录玩家 %player% 尝试移动，已进行回弹.",e.getPlayer().getName(),false);
            this.getLogger().info(msgNoLoginTryMove);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e){
        if (noLoginPlayerList.contains(e.getPlayer().getName()) && Configvar.noLoginPlayerCantBreak) {
            getLogger().info("未登录玩家 " + e.getPlayer().getName() + " 尝试挖掘方块" + e.getBlock().getType().name() + "已进行阻止.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {

            if (noLoginPlayerList.contains(player.getName()) && Configvar.noLoginPlayerCantHurt) {
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

        if (noLoginPlayerList.contains(e.getPlayer().getName())) {
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
            playerLocation.getWorld().spawnParticle(Particle.FIREWORK, particleLocation, 1, 0, 0, 0, 0.1); // 使用Firework粒子效果
        }

    }




//    @EventHandler
//    public void onPlayerInteract(PlayerInteractEvent e) {
//        if (noLoginPlayerList.contains(e.getPlayer().getName()) && e.getAction() == Action.LEFT_CLICK_AIR) {
//            this.getLogger().info("玩家 " + e.getPlayer().getName() + "成功登录, 已解除封锁.");
//            noLoginPlayerList.remove(e.getPlayer().getName());
//        }
//    }


}
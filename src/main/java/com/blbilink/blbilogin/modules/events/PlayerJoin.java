package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.modules.effects.Particles;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoin implements Listener {
    CheckOnline check = CheckOnline.INSTANCE;
    private final BlbiLogin plugin;
    public PlayerJoin(BlbiLogin plugin){
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent ev) {
        Player e = ev.getPlayer();
        addNoLoginList(e.getPlayer());
        if(!check.isAllowed(e)) {
            teleportLocation(e);
            setFlying(e);
            sendParticles(e);
        } else {
            LoginAction.INSTANCE.loginSuccess(e);
        }
    }

    private void addNoLoginList(Player player){
        plugin.getLogger().info(plugin.i18n.as("logPlayerJoin", false, player.getName()));
        Configvar.noLoginPlayerList.add(player.getName());
    }

    private void setFlying(Player player){
        if (Configvar.config.getBoolean("noLoginPlayerCantMove")) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    private void sendParticles(Player player){
        if (Configvar.config.getBoolean("noLoginPlayerParticle")) {
            Particles particles = new Particles();
            if (plugin.foliaUtil.isFolia) {
                // Folia 环境
                player.getScheduler().runAtFixedRate(plugin, task -> {
                    if (Configvar.noLoginPlayerList.contains(player.getName())) {
                        particles.createFallingParticlesAroundPlayer(player);
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
                            particles.createFallingParticlesAroundPlayer(player);
                        } else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0L, 20L);
            }
        }
    }
    private void teleportLocation(Player player){
        if(Configvar.config.getBoolean("playerJoinAutoTeleportToSavedLocation")){
            Location loc = new Location(
                    Bukkit.getWorld(Configvar.config.getString("locationPos.world")),
                    Configvar.config.getDouble("locationPos.x"),
                    Configvar.config.getDouble("locationPos.y"),
                    Configvar.config.getDouble("locationPos.z"),
                    (float) Configvar.config.getDouble("locationPos.yaw"),
                    (float) Configvar.config.getDouble("locationPos.pitch")
            );
            if(Configvar.config.getBoolean("playerJoinAutoTeleportToSavedLocation_AutoBack")){
                Configvar.originalLocation.put(player.getName(), player.getLocation());
            }
            if(plugin.foliaUtil.isFolia){
                player.getScheduler().run(plugin, task -> {
                    player.teleportAsync(loc);
                }, () -> {
                });
            }else{
                player.teleport(loc);
            }
        }
    }
}

package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.modules.effects.Particles;
import com.blbilink.blbilogin.vars.Configvar;
import io.papermc.paper.threadedregions.RegionizedServerInitEvent;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoin implements Listener {
    private final BlbiLogin plugin;
    private RegionScheduler regionScheduler;
    public PlayerJoin(BlbiLogin plugin){
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        teleportLocation(e.getPlayer());
        addNoLoginList(e.getPlayer());
        setFlying(e.getPlayer());
        sendParticles(e.getPlayer());
    }

    private void addNoLoginList(Player player){
        plugin.getLogger().info(plugin.i18n.as("logPlayerJoin", false, player.getName()));
        Configvar.noLoginPlayerList.add(player.getName());
    }

    private void setFlying(Player player){
        if (Configvar.noLoginPlayerCantMove) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    private void sendParticles(Player player){
        if (Configvar.noLoginPlayerParticle) {
            Particles particles = new Particles();
            if (Configvar.isFolia) {
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
    @EventHandler
    public void onRegionizedServerInit(RegionizedServerInitEvent event) {
        regionScheduler = plugin.getServer().getRegionScheduler();
    }
    private void teleportLocation(Player player){
        if(Configvar.playerJoinAutoTeleportToSavedLocation){
            Location loc = new Location(
                    Bukkit.getWorld(Configvar.location_world),
                    Configvar.location_x,
                    Configvar.location_y,
                    Configvar.location_z,
                    Configvar.location_yaw,
                    Configvar.location_pitch
            );
            if(Configvar.isFolia){
                regionScheduler.execute(plugin,loc, () -> player.teleportAsync(loc));
            }else{
                player.teleport(loc);
            }
        }
    }
}

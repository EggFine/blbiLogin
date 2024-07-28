package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.modules.effects.Particles;
import com.blbilink.blbilogin.vars.Configvar;
import org.blbilink.blbiLibrary.utils.FoliaUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerJoin implements Listener {
    CheckOnline check = CheckOnline.INSTANCE;
    private final BlbiLogin plugin;
    private final Map<String, FoliaUtil.Cancellable> particleTasks = new HashMap<>();

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

    private void sendParticles(Player player) {
        if (Configvar.config.getBoolean("noLoginPlayerParticle")) {
            Particles particles = new Particles();
            FoliaUtil.Cancellable task = plugin.foliaUtil.runTaskTimerAsync(plugin, cancellable -> {
                if (Configvar.noLoginPlayerList.contains(player.getName())) {
                    particles.createFallingParticlesAroundPlayer(player);
                } else {
                    cancellable.cancel();
                    particleTasks.remove(player.getName());
                }
            }, 0L, 20L);
            particleTasks.put(player.getName(), task);
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
            plugin.foliaUtil.runTask(plugin, task -> player.teleport(loc));
        }
    }
}
package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteraction implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();
        if (!playerCanInteract(player)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAttack(final EntityDamageByEntityEvent ev) {
        if (!(ev.getDamager() instanceof Player)) return;
        final Player attacker = (Player) ev.getDamager();
        Entity victim = ev.getEntity();

        if (!playerCanInteract(attacker)) {
            ev.setCancelled(true);
            return;
        }

        if (victim instanceof Player) {
            if (!playerCanInteract((Player) victim)) {
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent ev) {
        Player player = ev.getPlayer();
        if (!playerCanInteract(player)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent ev) {
        Player player = ev.getPlayer();
        if (!playerCanInteract(player)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent ev) {
        if (!(ev.getTarget() instanceof Player)) return;
        Player targetPlayer = (Player) ev.getTarget();
        if (!playerCanInteract(targetPlayer)) {
            ev.setCancelled(true);
        }
    }

    private boolean playerCanInteract(Player player) {
        boolean noLoginPlayerCantInteract = Configvar.config.getBoolean("noLoginPlayerCantInteract");
        boolean isPlayerInNoLoginList = Configvar.noLoginPlayerList.contains(player.getName());

        // 如果设置为true（未登录玩家不能交互），且玩家在未登录列表中，则不能交互
        return !(noLoginPlayerCantInteract && isPlayerInNoLoginList);
    }
}
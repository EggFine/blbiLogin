package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class PlayerInteraction implements Listener {

    @EventHandler(ignoreCancelled=true)
    public void onPlayerInteract(PlayerInteractEvent ev) {
        Player e = e.getPlayer();
        if(playerCanInteract(e)) {
            ev.setCancelled(true);
			return;
        }
    }
	
	@EventHandler(ignoreCancelled=true)
    public void onPlayerAttack(final EntityDamageByEntityEvent ev) {
		if (!(ev.getDamager() instanceof Player)) return;
		final Player e = (Player)ev.getDamager();
		Entity victim = ev.getEntity();
		
        if (!playerCanInteract(e)) {
            ev.setCancelled(true);
            return;
        }
		
		if (victim instanceof Player) {
			if (!playerCanInteract((Player) victim)) {
				ev.setCancelled(true);
				return;
			}
        }
    }
	
	@EventHandler(ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent ev) {
        Player e = ev.getPlayer();
		if(!(ev.getPlayer().getType() == EntityType.PLAYER)) return;
        if (!playerCanInteract(e)) {
            ev.setCancelled(true);
            return;
        }
	}
	
	@EventHandler(ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player e = ev.getPlayer();
		if(!(ev.getPlayer().getType() == EntityType.PLAYER)) return;
        if (!playerCanInteract(e)) {
            ev.setCancelled(true);
            return;
        }
	}
	
    @EventHandler(ignoreCancelled=true)
    public void onEntityTarget(EntityTargetEvent ev) {
        if (!(ev.getTarget() instanceof Player)) return;
        Player e = (Player) ev.getTarget();
        if (!playerCanInteract(e)) {
            ev.setCancelled(true);
        }
    }
	
	private boolean playerCanInteract(Player e) {
		return (Configvar.config.getBoolean("noLoginPlayerCantInteract") && Configvar.noLoginPlayerList.contains(e))
	}

}

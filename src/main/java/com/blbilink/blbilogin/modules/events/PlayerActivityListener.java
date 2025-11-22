package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerActivityListener implements Listener {

    private final BlbiLogin plugin;

    public PlayerActivityListener(BlbiLogin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (Configvar.noLoginPlayerList.contains(e.getPlayer().getName()) && Configvar.config.getBoolean("noLoginPlayerCantMove")) {
            String msgNoLoginTryMove = plugin.i18n.as("logNoLoginTryMove", false, e.getPlayer().getName());
            plugin.getLogger().info(msgNoLoginTryMove);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        if (Configvar.noLoginPlayerList.contains(e.getPlayer().getName()) && Configvar.config.getBoolean("noLoginPlayerCantBreak")) {
            plugin.getLogger().info("未登录玩家 " + e.getPlayer().getName() + " 尝试挖掘方块" + e.getBlock().getType().name() + "已进行阻止.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (Configvar.noLoginPlayerList.contains(player.getName()) && Configvar.config.getBoolean("noLoginPlayerCantHurt")) {
                EntityDamageEvent.DamageCause currentDamageCause = e.getCause();
                String damageCauseName = currentDamageCause.name();

                plugin.getLogger().info("未登录玩家 " + player.getName() + " 受到伤害 " + damageCauseName + " 已进行阻止.");
                e.setCancelled(true);
            }
        }
    }
}

package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.modules.Configvar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerUseCommands implements Listener {
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
}

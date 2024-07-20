package com.blbilink.blbilogin.modules.events;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

public class FloodgateCheck {
	public static boolean isFloodgate(Player player) {
		try {
			FloodgateApi api = FloodgateApi.getInstance();
			return api.isFloodgatePlayer(player.getUniqueId());
		} catch (Exception e) {
			return false;
		}
	}
}

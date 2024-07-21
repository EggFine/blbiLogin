package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum CheckOnline {
	INSTANCE;
    public BlbiLogin plugin;
	private Boolean floodgate = false;
	private Boolean useBedrock = false;
	
    public void sync(BlbiLogin plugin){
        this.plugin = plugin;
		if (Bukkit.getPluginManager().isPluginEnabled("Floodgate")) this.floodgate = true;
    }

    public boolean isAllowed(Player e){		
        if (verifyAllowBedrock(e)) return true;
		return false;
    }
	
	public boolean verifyAllowBedrock(Player e) {
		boolean isBedrock = false;
		isBedrock = (e.getUniqueId().toString().startsWith("00000000-0000-0000-") && Configvar.config.getBoolean("bedrock.autologin.uuid"));
		if (isBedrock) return true;
		isBedrock = (e.getName().startsWith(Configvar.config.getString("bedrock.autologin.prefix_value")) && Configvar.config.getBoolean("bedrock.autologin.prefix"));
		if (isBedrock) return true;
		if(floodgate == true && Configvar.config.getBoolean("bedrock.autologin.floodgate")) {
			try { isBedrock = FloodgateCheck.isFloodgate(e); if (isBedrock) return true; } catch (Exception ignored) {}
		}
		return isBedrock;
	}
	
	public boolean isBedrock(Player e) {
		if(!Configvar.config.getBoolean("bedrock.forms")) return false;
		boolean isBedrock = false;
		isBedrock = (e.getUniqueId().toString().startsWith("00000000-0000-0000-"));
		if (isBedrock) return true;
		isBedrock = (e.getName().startsWith(Configvar.config.getString("bedrock.autologin.prefix_value")) && Configvar.config.getBoolean("bedrock.autologin.prefix"));
		if (isBedrock) return true;
		if(floodgate == true) {
			try { isBedrock = FloodgateCheck.isFloodgate(e); if (isBedrock) return true; } catch (Exception ignored) {}
		}
		return isBedrock;
	}
}

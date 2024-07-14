//package com.blbilink.blbilogin.modules;
//
//import com.blbilink.blbilogin.BlbiLogin;
//import com.comphenix.protocol.PacketType;
//import com.comphenix.protocol.ProtocolLibrary;
//import com.comphenix.protocol.ProtocolManager;
//import com.comphenix.protocol.events.PacketAdapter;
//import com.comphenix.protocol.events.PacketEvent;
//import org.bukkit.entity.Player;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class CheckOnline {
//    private final BlbiLogin plugin;
//    private final Map<UUID, Boolean> premiumPlayers = new HashMap<>();
//    private final ProtocolManager protocolManager;
//
//    public CheckOnline(BlbiLogin plugin) {
//        this.plugin = plugin;
//        this.protocolManager = ProtocolLibrary.getProtocolManager();
//        registerLoginListener();
//    }
//
//    private void registerLoginListener() {
//        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Login.Server.ENCRYPTION_BEGIN) {
//            @Override
//            public void onPacketSending(PacketEvent event) {
//                Player player = event.getPlayer();
//                if (player != null) {
//                    UUID uuid = player.getUniqueId();
//                    premiumPlayers.put(uuid, true);
//                }
//            }
//        });
//    }
//
//    public boolean isPremiumPlayer(Player player) {
//        return premiumPlayers.getOrDefault(player.getUniqueId(), false);
//    }
//
//    public void removePlayer(Player player) {
//        premiumPlayers.remove(player.getUniqueId());
//    }
//}
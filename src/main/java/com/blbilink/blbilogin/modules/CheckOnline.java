//package com.blbilink.blbilogin.modules;
//
//import com.comphenix.protocol.PacketType;
//import com.comphenix.protocol.ProtocolLibrary;
//import com.comphenix.protocol.ProtocolManager;
//import com.comphenix.protocol.events.PacketAdapter;
//import com.comphenix.protocol.events.PacketContainer;
//import com.comphenix.protocol.events.PacketEvent;
//import com.comphenix.protocol.wrappers.WrappedGameProfile;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerLoginEvent;
//import org.bukkit.event.player.PlayerQuitEvent;
//import org.bukkit.plugin.Plugin;
//
//import java.net.InetSocketAddress;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class CheckOnline implements Listener {
//    private static final Map<InetSocketAddress, ConnectionInfo> connectionInfoMap = new ConcurrentHashMap<>();
//    private static final Map<String, PlayerInfo> playerInfoMap = new ConcurrentHashMap<>();
//    private static Plugin plugin;
//    private static ProtocolManager protocolManager;
//
//    private static class ConnectionInfo {
//        boolean premiumAuthentication = false;
//        String username;
//        boolean onlineMode = false;
//    }
//
//    private static class PlayerInfo {
//        boolean isPremium = false;
//        UUID realUUID;
//    }
//
//    public static void initialize(Plugin plugin) {
//        CheckOnline.plugin = plugin;
//        protocolManager = ProtocolLibrary.getProtocolManager();
//        plugin.getServer().getPluginManager().registerEvents(new CheckOnline(), plugin);
//        registerPacketListeners();
//        plugin.getLogger().info("CheckOnline initialized");
//    }
//
//    private static void registerPacketListeners() {
//        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Handshake.Client.SET_PROTOCOL) {
//            @Override
//            public void onPacketReceiving(PacketEvent event) {
//                PacketContainer packet = event.getPacket();
//                try {
//                    int protocolVersion = packet.getIntegers().read(0);
//                    String serverAddress = packet.getStrings().read(0);
//                    int serverPort = packet.getIntegers().read(1);
//
//                    plugin.getLogger().info("Handshake received: protocol=" + protocolVersion +
//                            ", serverAddress=" + serverAddress +
//                            ", port=" + serverPort);
//
//                    // We don't modify the packet here anymore
//                } catch (Exception e) {
//                    plugin.getLogger().warning("Error processing Handshake packet: " + e.getMessage());
//                }
//            }
//        });
//
//        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Login.Client.START) {
//            @Override
//            public void onPacketReceiving(PacketEvent event) {
//                String username = event.getPacket().getStrings().read(0);
//                InetSocketAddress address = event.getPlayer().getAddress();
//                ConnectionInfo info = new ConnectionInfo();
//                info.username = username;
//                info.onlineMode = true; // Initially set to true
//                connectionInfoMap.put(address, info);
//                plugin.getLogger().info("Login START received for " + username);
//            }
//        });
//
//        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Login.Client.ENCRYPTION_BEGIN) {
//            @Override
//            public void onPacketReceiving(PacketEvent event) {
//                InetSocketAddress address = event.getPlayer().getAddress();
//                ConnectionInfo info = connectionInfoMap.get(address);
//                if (info != null) {
//                    info.premiumAuthentication = true;
//                    plugin.getLogger().info("ENCRYPTION_BEGIN received for " + info.username);
//                }
//            }
//        });
//
//        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Login.Server.SUCCESS) {
//            @Override
//            public void onPacketSending(PacketEvent event) {
//                try {
//                    WrappedGameProfile profile = event.getPacket().getGameProfiles().read(0);
//                    String username = profile.getName();
//                    UUID uuid = profile.getUUID();
//                    InetSocketAddress address = event.getPlayer().getAddress();
//                    ConnectionInfo info = connectionInfoMap.remove(address);
//
//                    if (info != null && info.premiumAuthentication) {
//                        PlayerInfo playerInfo = new PlayerInfo();
//                        playerInfo.isPremium = true;
//                        playerInfo.realUUID = uuid;
//                        playerInfoMap.put(username.toLowerCase(), playerInfo);
//                        plugin.getLogger().info("Login SUCCESS for premium player: " + username + ", UUID: " + uuid);
//                    } else {
//                        plugin.getLogger().info("Login SUCCESS for non-premium player: " + username + ", UUID: " + uuid);
//                    }
//                } catch (Exception e) {
//                    plugin.getLogger().warning("Error processing Login SUCCESS packet: " + e.getMessage());
//                }
//            }
//        });
//    }
//
//    @EventHandler
//    public void onPlayerLogin(PlayerLoginEvent event) {
//        Player player = event.getPlayer();
//        String username = player.getName().toLowerCase();
//        PlayerInfo info = playerInfoMap.get(username);
//
//        if (info != null && info.isPremium) {
//            plugin.getLogger().info("Premium player " + username + " logged in with UUID: " + info.realUUID);
//        } else {
//            plugin.getLogger().info("Non-premium player " + username + " logged in");
//        }
//    }
//
//    @EventHandler
//    public void onPlayerQuit(PlayerQuitEvent event) {
//        playerInfoMap.remove(event.getPlayer().getName().toLowerCase());
//    }
//
//    public static boolean isPlayerPremium(Player player) {
//        PlayerInfo info = playerInfoMap.get(player.getName().toLowerCase());
//        return info != null && info.isPremium;
//    }
//
//    public static void cleanup() {
//        connectionInfoMap.clear();
//        playerInfoMap.clear();
//        plugin.getLogger().info("CheckOnline cleaned up");
//    }
//}
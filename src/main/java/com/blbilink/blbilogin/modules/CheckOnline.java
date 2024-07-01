//package com.blbilink.blbilogin.modules;
//
//import com.comphenix.protocol.PacketType;
//import com.comphenix.protocol.ProtocolLibrary;
//import com.comphenix.protocol.events.PacketAdapter;
//import com.comphenix.protocol.events.PacketContainer;
//import com.comphenix.protocol.events.PacketEvent;
//import com.blbilink.blbilogin.BlbiLogin;
//import org.bukkit.entity.Player;
//
//import javax.crypto.Cipher;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.math.BigInteger;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.security.*;
//import java.time.Duration;
//import java.util.Arrays;
//import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.logging.Level;
//
//public class CheckOnline {
//    private static final String MOJANG_SESSION_SERVER = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s";
//    private static BlbiLogin plugin;
//    private static final ConcurrentHashMap<String, LoginSession> loginSessions = new ConcurrentHashMap<>();
//    private static final HttpClient httpClient = HttpClient.newHttpClient();
//
//    public static void initialize(BlbiLogin pluginInstance) {
//        plugin = pluginInstance;
//        ProtocolLibrary.getProtocolManager().addPacketListener(
//                new PacketAdapter(plugin, PacketType.Login.Client.START, PacketType.Login.Client.ENCRYPTION_BEGIN) {
//                    @Override
//                    public void onPacketReceiving(PacketEvent event) {
//                        if (event.getPacketType() == PacketType.Login.Client.START) {
//                            handleLoginStart(event);
//                        } else if (event.getPacketType() == PacketType.Login.Client.ENCRYPTION_BEGIN) {
//                            handleEncryptionBegin(event);
//                        }
//                    }
//                }
//        );
//    }
//
//    private static void handleLoginStart(PacketEvent event) {
//        String playerName = event.getPacket().getStrings().read(0);
//        LoginSession session = new LoginSession();
//        loginSessions.put(playerName, session);
//
//        plugin.getLogger().info("玩家开始登录: " + playerName);
//
//        try {
//            KeyPair keyPair = generateKeyPair();
//            session.setKeyPair(keyPair);
//
//            byte[] token = new byte[4];
//            new Random().nextBytes(token);
//            session.setToken(token);
//
//            PacketContainer packet = new PacketContainer(PacketType.Login.Server.ENCRYPTION_BEGIN);
//            packet.getStrings().write(0, ""); // 服务器ID（空字符串）
//            packet.getByteArrays().write(0, keyPair.getPublic().getEncoded()); // 公钥
//            packet.getByteArrays().write(1, token); // 验证令牌
//
//            ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), packet);
//            plugin.getLogger().info("已发送加密请求给玩家: " + playerName);
//        } catch (Exception e) {
//            plugin.getLogger().log(Level.SEVERE, "玩家登录开始时出错: " + playerName, e);
//        }
//    }
//
//    private static void handleEncryptionBegin(PacketEvent event) {
//        String playerName = event.getPlayer().getName();
//        LoginSession session = loginSessions.get(playerName);
//
//        if (session == null) {
//            plugin.getLogger().warning("未找到玩家的登录会话: " + playerName);
//            return;
//        }
//
//        try {
//            plugin.getLogger().info("收到玩家的加密响应: " + playerName);
//
//            PacketContainer packet = event.getPacket();
//            byte[] sharedSecret = packet.getByteArrays().read(0);
//            byte[] verifyToken = packet.getByteArrays().read(1);
//
//            PrivateKey privateKey = session.getKeyPair().getPrivate();
//            SecretKey secretKey = decryptSharedSecret(privateKey, sharedSecret);
//            byte[] decryptedVerifyToken = decryptVerifyToken(privateKey, verifyToken);
//
//            if (!Arrays.equals(decryptedVerifyToken, session.getToken())) {
//                plugin.getLogger().warning("玩家的验证令牌无效: " + playerName);
//                throw new IllegalStateException("玩家的验证令牌无效: " + playerName);
//            }
//
//            String serverId = generateServerId(secretKey, session.getKeyPair().getPublic());
//
//            if (hasJoinedServer(playerName, serverId)) {
//                plugin.getLogger().info("正版玩家验证成功: " + playerName);
//                plugin.noLoginPlayerList.remove(playerName);
//            } else {
//                plugin.getLogger().info("非正版玩家: " + playerName);
//                plugin.noLoginPlayerList.add(playerName);
//            }
//
//            // 允许登录过程继续
//            event.setCancelled(false);
//        } catch (Exception e) {
//            plugin.getLogger().log(Level.SEVERE, "玩家加密开始时出错: " + playerName, e);
//            plugin.noLoginPlayerList.add(playerName);
//            event.setCancelled(true);
//        } finally {
//            loginSessions.remove(playerName);
//        }
//    }
//
//    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(1024);
//        return keyGen.generateKeyPair();
//    }
//
//    private static SecretKey decryptSharedSecret(PrivateKey privateKey, byte[] sharedSecret) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        return new SecretKeySpec(cipher.doFinal(sharedSecret), "AES");
//    }
//
//    private static byte[] decryptVerifyToken(PrivateKey privateKey, byte[] verifyToken) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        return cipher.doFinal(verifyToken);
//    }
//
//    private static String generateServerId(SecretKey secretKey, PublicKey publicKey) throws Exception {
//        MessageDigest digest = MessageDigest.getInstance("SHA-1");
//        digest.update(secretKey.getEncoded());
//        digest.update(publicKey.getEncoded());
//        return new BigInteger(digest.digest()).toString(16);
//    }
//
//    private static boolean hasJoinedServer(String username, String serverId) {
//        String url = String.format(MOJANG_SESSION_SERVER, username, serverId);
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .timeout(Duration.ofSeconds(5))
//                .build();
//
//        try {
//            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            return response.statusCode() == 200;
//        } catch (Exception e) {
//            plugin.getLogger().log(Level.SEVERE, "Error checking player premium status: " + username, e);
//            return false;
//        }
//    }
//
//    public static boolean isPlayerPremium(Player player) {
//        return !plugin.noLoginPlayerList.contains(player.getName());
//    }
//
//    private static class LoginSession {
//        private KeyPair keyPair;
//        private byte[] token;
//
//        public void setKeyPair(KeyPair keyPair) {
//            this.keyPair = keyPair;
//        }
//
//        public KeyPair getKeyPair() {
//            return keyPair;
//        }
//
//        public void setToken(byte[] token) {
//            this.token = token;
//        }
//
//        public byte[] getToken() {
//            return token;
//        }
//    }
//}

package com.blbilink.blbilogin.modules.messages;

import com.blbilink.blbilogin.modules.FoliaCompat;
import com.blbilink.blbilogin.modules.Sqlite;
import com.blbilink.blbilogin.modules.events.CheckOnline;
import com.blbilink.blbilogin.modules.events.LoginAction;
import com.blbilink.blbilogin.vars.Configvar;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.Timer;
import java.util.TimerTask;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class PlayerSender implements Listener {
    CheckOnline check = CheckOnline.INSTANCE;
    LoginAction login = LoginAction.INSTANCE;
    private Timer timer;

    @EventHandler
    public void noLoginPlayerSendTitle(PlayerJoinEvent e) {
        if (Configvar.config.getBoolean("noLoginPlayerSendActionBar") ||
                Configvar.config.getBoolean("noLoginPlayerSendTitle") ||
                Configvar.config.getBoolean("noLoginPlayerSendSubTitle") ||
                Configvar.config.getBoolean("noLoginPlayerSendMessage") ||
                Configvar.config.getBoolean("noRegisterPlayerSendTitle") ||
                Configvar.config.getBoolean("noRegisterPlayerSendSubTitle") ||
                Configvar.config.getBoolean("noRegisterPlayerSendMessage") ||
                Configvar.config.getBoolean("noRegisterPlayerSendActionBar")) {

            Player player = e.getPlayer();

            if (!check.isBedrock(e.getPlayer())) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() { if (Configvar.noLoginPlayerList.contains(e.getPlayer().getName())) { sendPlayerMessages(e.getPlayer()); } else if (!e.getPlayer().isOnline()) { if (timer != null) { timer.cancel(); } } else { if (timer != null) { timer.cancel(); } } }
                }, 0L, 1000L);
            } else {
                FoliaCompat.runTaskLater(plugin, () -> openForum(e.getPlayer()), 60L);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent ev) {
        Player e = ev.getPlayer();
        if(Configvar.noLoginPlayerList.contains(e.getName())) { Configvar.noLoginPlayerList.remove(e.getName()); }
        if (timer != null) { timer.cancel(); }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(plugin)) if (timer != null) timer.cancel();
    }

    public void openForum(Player player) {
        boolean register = false;
        if(Sqlite.getSqlite().playerExists(player.getUniqueId().toString())) { register = true; }

        String title = "";
        String description = "";
        String field_title = "";
        String field_example = "";

        if(register) {
            title = "Login";
            description = "Please, insert your password to login.";
            field_title = "Password";
            field_example = "Insert password.";
        } else {
            title = "Register";
            description = "Please, insert your password to register a new player.";
            field_title = "Password";
            field_example = "Insert password.";
        }

        FloodgatePlayer eF = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        if(eF == null) return;
        CustomForm.Builder builder = CustomForm.builder()
                .title(title)
                .label(description)
                .input(field_title, field_example);
        builder = builder.validResultHandler(response -> {
            String pass = response.asInput();
            if (!pass.isEmpty()){
                verify(player, pass);
            } else {
                openForum(player);
            }
        });
        builder = builder.closedOrInvalidResultHandler(() -> openForum(player));

        eF.sendForm(builder.build());
    }

    protected void verify(Player e, String password) {
        String uuid = e.getUniqueId().toString();
        if (!Sqlite.getSqlite().playerExists(uuid)) {
            Sqlite.getSqlite().registerPlayer(uuid, e.getName(), password);
            openForum(e);
        } else {
            if (login.isCorrect(uuid, password)) {
                login.loginSuccess(e);
            } else {
                openForum(e);
            }
        }
    }

    private static void sendPlayerMessages(Player player) {
        if(Sqlite.getSqlite().playerExists(player.getUniqueId().toString())) {
            if(Configvar.config.getBoolean("noLoginPlayerSendActionBar")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(plugin.i18n.as("noLoginPlayerSendActionBar",false, player.getName())));
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendMessage")) {
                player.sendMessage(plugin.i18n.as("noLoginPlayerSendMessage", true, player.getName()));
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendTitle")) {
                player.sendTitle(plugin.i18n.as("noLoginPlayerSendTitle", false, player.getName()), null, 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendSubTitle")) {
                player.sendTitle(null, plugin.i18n.as("noLoginPlayerSendSubTitle", false, player.getName()), 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendMessage") || Configvar.config.getBoolean("noLoginPlayerSendActionBar")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            }
        } else {
            if(Configvar.config.getBoolean("noRegisterPlayerSendActionBar")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(plugin.i18n.as("noRegisterPlayerSendActionBar",false, player.getName())));
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendMessage")) {
                player.sendMessage(plugin.i18n.as("noRegisterPlayerSendMessage", true, player.getName()));
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendTitle")) {
                player.sendTitle(plugin.i18n.as("noRegisterPlayerSendTitle", false, player.getName()), null, 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendSubTitle")) {
                player.sendTitle(null, plugin.i18n.as("noRegisterPlayerSendSubTitle", false,player.getName()), 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendMessage") || Configvar.config.getBoolean("noRegisterPlayerSendActionBar")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            }
        }
    }
}
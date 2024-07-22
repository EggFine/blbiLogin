package com.blbilink.blbilogin.load;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.modules.Sqlite;
import com.blbilink.blbilogin.modules.commands.BlbiLoginCommand;
import com.blbilink.blbilogin.modules.commands.Login;
import com.blbilink.blbilogin.modules.commands.Register;
import com.blbilink.blbilogin.modules.commands.ResetPassword;
import com.blbilink.blbilogin.modules.events.PlayerJoin;
import com.blbilink.blbilogin.modules.events.PlayerSendMessage;
import com.blbilink.blbilogin.modules.events.PlayerUseCommands;
import com.blbilink.blbilogin.modules.events.PlayerInteraction;
import com.blbilink.blbilogin.modules.messages.PlayerSender;
import org.bukkit.Bukkit;

import java.util.Objects;

public class LoadFunction {
    private final BlbiLogin plugin;
    public static Sqlite sqlite;
    public LoadFunction(BlbiLogin plugin) {
        this.plugin = plugin;
    }

    public void loadFunction(){
        loadCommands();
        loadListeners();
        loadSqlite();
    }

    private void loadCommands(){
        BlbiLoginCommand blbiLoginCommand = new BlbiLoginCommand();
        Objects.requireNonNull(plugin.getCommand("blbilogin")).setExecutor(blbiLoginCommand);
        Objects.requireNonNull(plugin.getCommand("blbilogin")).setTabCompleter(blbiLoginCommand);


        Objects.requireNonNull(plugin.getCommand("register")).setExecutor(new Register());
        Objects.requireNonNull(plugin.getCommand("login")).setExecutor(new Login());
        Objects.requireNonNull(plugin.getCommand("resetpassword")).setExecutor(new ResetPassword());
    }
    private void loadListeners(){
        // 注册Bukkit事件监听器
        Bukkit.getPluginManager().registerEvents(BlbiLogin.plugin, plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerSender(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerUseCommands(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(BlbiLogin.plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerSendMessage(), plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerInteraction(), plugin);
    }

    private void loadSqlite(){
        // 初始化sqlite数据库
        sqlite = new Sqlite();
    }

}

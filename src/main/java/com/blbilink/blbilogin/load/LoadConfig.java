package com.blbilink.blbilogin.load;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.vars.Configvar;
import org.blbilink.blbiLibrary.I18n;
import org.blbilink.blbiLibrary.utils.FileUtil;
import org.blbilink.blbiLibrary.utils.YmlUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class LoadConfig {
    public static void loadConfig(BlbiLogin plugin) {
        // 加载配置
        plugin.getLogger().info("开始加载配置文件");
        Configvar.configFile = new File(plugin.getDataFolder(), "config.yml");
        Configvar.config = YamlConfiguration.loadConfiguration(Configvar.configFile);
        FileConfiguration configNew = YamlConfiguration.loadConfiguration(new InputStreamReader(Objects.requireNonNull(plugin.getResource("config.yml"))));
        if (YmlUtil.checkVersion(configNew.getString("version"), Configvar.config.getString("version"))) {
            Configvar.config.set("version", configNew.getString("version"));
            try {
                Configvar.config.save(Configvar.configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            plugin.getLogger().warning("检测到新版本配置文件");
            FileUtil.completeFile(plugin,"config.yml", (String) null);

        } else {
            plugin.getLogger().info("未检测到新版本配置文件");
        }
        Configvar.language = Configvar.config.getString("language", "zh_CN");
        Configvar.prefix = Configvar.config.getString("prefix", "§8[§fblbi§bLogin§8] ");

        Configvar.noLoginPlayerCantMove = Configvar.config.getBoolean("noLoginPlayerCantMove", true);
        Configvar.noLoginPlayerCantUseCommand = Configvar.config.getBoolean("noLoginPlayerCantUseCommand", true);
        Configvar.noLoginPlayerAllowUseCommand = Configvar.config.getStringList("noLoginPlayerAllowUseCommand");
        Configvar.noLoginPlayerCantBreak = Configvar.config.getBoolean("noLoginPlayerCantBreak", true);
        Configvar.noLoginPlayerCantHurt = Configvar.config.getBoolean("noLoginPlayerCantHurt", true);
        Configvar.noLoginPlayerParticle = Configvar.config.getBoolean("noLoginPlayerParticle", true);

        Configvar.noLoginPlayerSendMessage = Configvar.config.getBoolean("noLoginPlayerSendMessage", true);
        Configvar.noLoginPlayerSendTitle = Configvar.config.getBoolean("noLoginPlayerSendTitle", true);
        Configvar.noLoginPlayerSendSubTitle = Configvar.config.getBoolean("noLoginPlayerSendSubTitle", true);
        Configvar.noLoginPlayerSendActionBar = Configvar.config.getBoolean("noLoginPlayerSendActionBar", true);
        Configvar.noRegisterPlayerSendMessage = Configvar.config.getBoolean("noRegisterPlayerSendMessage", true);
        Configvar.noRegisterPlayerSendTitle = Configvar.config.getBoolean("noRegisterPlayerSendTitle", true);
        Configvar.noRegisterPlayerSendSubTitle = Configvar.config.getBoolean("noRegisterPlayerSendSubTitle", true);
        Configvar.noRegisterPlayerSendActionBar = Configvar.config.getBoolean("noRegisterPlayerSendActionBar", true);

        Configvar.successLoginSendTitle = Configvar.config.getBoolean("successLoginSendTitle", true);
        Configvar.successLoginSendSubTitle = Configvar.config.getBoolean("successLoginSendSubTitle", true);

        Configvar.playerJoinAutoTeleportToSavedLocation = Configvar.config.getBoolean("playerJoinAutoTeleportToSavedLocation", false);
        Configvar.playerJoinAutoTeleportToSavedLocation_AutoBack = Configvar.config.getBoolean("playerJoinAutoTeleportToSavedLocation_AutoBack", false);
        Configvar.location_world = Configvar.config.getString("locationPos.world", "world");
        Configvar.location_x = Configvar.config.getDouble("locationPos.x", 0.0);
        Configvar.location_y = Configvar.config.getDouble("locationPos.y", 0.0);
        Configvar.location_z = Configvar.config.getDouble("locationPos.z", 0.0);
        Configvar.location_yaw = (float) Configvar.config.getDouble("locationPos.yaw", 0.0);
        Configvar.location_pitch = (float) Configvar.config.getDouble("locationPos.pitch", 0.0);
        plugin.i18n = new I18n(plugin,Configvar.prefix, Configvar.language);
        plugin.i18n.loadLanguage();
    }
}

// 备用: 加载多配置文件

// 获取或创建配置文件
// 每次运行插件导出一次配置文件模板
//plugin.saveResource("config.yml", true);
        /*
        plugin.getDataFolder().mkdirs(); // 确保数据文件夹存在
        if (!configFile.exists()) {
            // 如果文件不存在，从资源中复制模板，这里直接使用config.yml.template的名字作为resourcePath
            try {
                File templateFile = new File(plugin.getDataFolder(), "config.yml");
                Files.copy(templateFile.toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        }
         */

package com.blbilink.blbilogin.load;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.modules.Configvar;
import org.blbilink.blbiLibrary.I18n;
import org.blbilink.blbiLibrary.utils.FileUtil;
import org.blbilink.blbiLibrary.utils.YmlUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Load {
    private static FileConfiguration config;
    private static File configFile;
    private static FileConfiguration languageConfig;
    public static void loadConfig(BlbiLogin plugin) {
        // 加载配置
        plugin.getLogger().info("开始加载配置文件");
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        FileConfiguration configNew = YamlConfiguration.loadConfiguration(new InputStreamReader(Objects.requireNonNull(plugin.getResource("config.yml"))));
        if (YmlUtil.checkVersion(configNew.getString("version"), config.getString("version"))) {
            config.set("version", configNew.getString("version"));
            try {
                config.save(configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            plugin.getLogger().warning("检测到新版本配置文件");
            FileUtil.completeFile(plugin,"config.yml", (String) null);

        } else {
            plugin.getLogger().info("未检测到新版本配置文件");
        }
        Configvar.language = config.getString("language", "zh_CN");
        Configvar.prefix = config.getString("prefix", "§8[§fblbi§bLogin§8] ");

        Configvar.noLoginPlayerCantMove = config.getBoolean("noLoginPlayerCantMove", true);
        Configvar.noLoginPlayerCantUseCommand = config.getBoolean("noLoginPlayerCantUseCommand", true);
        Configvar.noLoginPlayerAllowUseCommand = config.getStringList("noLoginPlayerAllowUseCommand");
        Configvar.noLoginPlayerCantBreak = config.getBoolean("noLoginPlayerCantBreak", true);
        Configvar.noLoginPlayerCantHurt = config.getBoolean("noLoginPlayerCantHurt", true);
        Configvar.noLoginPlayerParticle = config.getBoolean("noLoginPlayerParticle", true);

        Configvar.noLoginPlayerSendMessage = config.getBoolean("noLoginPlayerSendMessage", true);
        Configvar.noLoginPlayerSendTitle = config.getBoolean("noLoginPlayerSendTitle", true);
        Configvar.noLoginPlayerSendSubTitle = config.getBoolean("noLoginPlayerSendSubTitle", true);
        Configvar.noLoginPlayerSendActionBar = config.getBoolean("noLoginPlayerSendActionBar", true);
        Configvar.noRegisterPlayerSendMessage = config.getBoolean("noRegisterPlayerSendMessage", true);
        Configvar.noRegisterPlayerSendTitle = config.getBoolean("noRegisterPlayerSendTitle", true);
        Configvar.noRegisterPlayerSendSubTitle = config.getBoolean("noRegisterPlayerSendSubTitle", true);
        Configvar.noRegisterPlayerSendActionBar = config.getBoolean("noRegisterPlayerSendActionBar", true);

        Configvar.successLoginSendTitle = config.getBoolean("successLoginSendTitle", true);
        Configvar.successLoginSendSubTitle = config.getBoolean("successLoginSendSubTitle", true);

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

package com.blbilink.blbilogin.load;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.vars.Configvar;
import org.blbilink.blbiLibrary.I18n;
import org.blbilink.blbiLibrary.utils.ConfigUtil;

public class LoadConfig {
    public static void loadConfig(BlbiLogin plugin) {
        // 加载配置
        plugin.config = new ConfigUtil(plugin);
        Configvar.config = plugin.config.loadConfig("config.yml");
        Configvar.configFile = plugin.config.configFile;
        plugin.i18n = new I18n(plugin,Configvar.config.getString("prefix"), Configvar.config.getString("language","zh_CN"));
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

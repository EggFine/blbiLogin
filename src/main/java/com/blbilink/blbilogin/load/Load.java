package com.blbilink.blbilogin.load;
import com.blbilink.blbilogin.modules.Configvar;
//导入Bukkit配置文件包
import com.blbilink.blbilogin.BlbiLogin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;

public class Load {
    private static FileConfiguration config;
    private static File configFile;
    private static FileConfiguration languageConfig;

    public static void loadConfig(BlbiLogin plugin){

        // 加载配置

        plugin.getLogger().info("开始加载配置文件");
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        Configvar.language  = config.getString("language", "zh_CN") ;
        loadLanguage(plugin);
        plugin.getLogger().info(String.format(getMessage("loadedLanguage","已加载语言文件: %s",null,false),Configvar.language) + " | " + getMessage("Language","简体中文 (Simplified Chinese)",null,false));
        Configvar.prefix = config.getString("prefix", "§8[§fblbi§bLogin§8] ") ;

        Configvar.noLoginPlayerCantMove = config.getBoolean("noLoginPlayerCantMove", true) ;
        Configvar.noLoginPlayerCantUseCommand = config.getBoolean("noLoginPlayerCantUseCommand", true) ;
        Configvar.noLoginPlayerAllowUseCommand = config.getStringList("noLoginPlayerAllowUseCommand") ;
        Configvar.noLoginPlayerCantBreak = config.getBoolean("noLoginPlayerCantBreak", true) ;
        Configvar.noLoginPlayerCantHurt = config.getBoolean("noLoginPlayerCantHurt", true) ;
        Configvar.noLoginPlayerParticle = config.getBoolean("noLoginPlayerParticle", true) ;

        Configvar.noLoginPlayerSendMessage = config.getBoolean("noLoginPlayerSendMessage", true) ;
        Configvar.noLoginPlayerSendTitle = config.getBoolean("noLoginPlayerSendTitle", true) ;
        Configvar.noLoginPlayerSendSubTitle = config.getBoolean("noLoginPlayerSendSubTitle", true) ;
        Configvar.noLoginPlayerSendActionBar = config.getBoolean("noLoginPlayerSendActionBar", true) ;

        Configvar.successLoginSendTitle = config.getBoolean("successLoginSendTitle", true) ;
        Configvar.successLoginSendSubTitle = config.getBoolean("successLoginSendSubTitle", true) ;
    }

    public static void loadLanguage(BlbiLogin plugin){
        String languageFileName = Configvar.language + ".yaml";
        File languageFolder = new File(plugin.getDataFolder(), "languages");
        if (!languageFolder.exists()) {
            languageFolder.mkdirs();
        }
        File languageFile = new File(languageFolder, languageFileName);

        // 检查语言文件是否存在，如果不存在则尝试从插件资源库获取
        if (!languageFile.exists()) {
            // 检查插件资源库是否存在用户配置的语言
            InputStream resourceStream = plugin.getResource("languages/" + languageFileName);
            if (resourceStream != null) {
                // 插件内有指定的语言文件，导出该文件
                plugin.saveResource("languages/" + languageFileName, false);
            } else {
                // 插件内没有指定的语言文件，使用默认的 zh_CN.yaml
                plugin.getLogger().warning("指定的语言文件 '" + languageFileName + "' 不存在，尝试使用默认的 zh_CN.yaml");
                plugin.getLogger().warning("The specified language file '" + languageFileName + "' does not exist, try using the default zh_CN.yaml");
                Configvar.language = "zh_CN";
                languageFileName = Configvar.language + ".yaml";
                languageFile = new File(languageFolder, languageFileName);
                if (!languageFile.exists()) {
                    // 如果默认语言文件也不存在，从插件资源中提取
                    plugin.saveResource("languages/" + languageFileName, false);
                }
            }
        }
        // 加载语言文件
        languageConfig = YamlConfiguration.loadConfiguration(languageFile);
    }

    public static String getMessage(String path, String defaultValue,@Nullable String player,boolean addPrefix) {
        String str = languageConfig.getString(path, defaultValue).replace("&", "§");
        if (player != null) {
            str = str.replace("%player%", player);
        }
        if (addPrefix) str = Configvar.prefix + str;
        return str;
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

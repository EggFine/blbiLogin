package com.blbilink.blbilogin.load;

//导入Bukkit配置文件包
import com.blbilink.blbilogin.BlbiLogin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Load {
    private FileConfiguration config;
    private File configFile;
    private FileConfiguration languageConfig;
    public void loadConfig(BlbiLogin plugin){
        // 获取或创建配置文件
        // 每次运行插件导出一次配置文件模板
        //plugin.saveResource("config.yml", true);
        configFile = new File(plugin.getDataFolder(), "config.yml");
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
        // 加载配置
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public void loadLanguage(BlbiLogin plugin){
        // 加载语言文件
        String language = config.getString("language", "zh_CN") ;
        String languageFileName = language + ".yaml";
        // 检查并创建默认语言文件
        File languageFolder = new File(plugin.getDataFolder(), "languages");
        languageFolder.mkdirs(); // 确保语言文件夹存在
        File defaultLanguageFile = new File(languageFolder, languageFileName);
        if (!defaultLanguageFile.exists()) {
            plugin.saveResource("languages/" + languageFileName, false); // 假定语言文件模板位于resources/languages/下
        }

        // 检查并加载指定语言文件
        File languageFile = new File(languageFolder, languageFileName);

        // 加载语言文件
        languageConfig = YamlConfiguration.loadConfiguration(languageFile);

        // 使用读取的语言文件
        String welcomeMessage = getMessage("msgEnable", "波比登录系统已注入。");
        plugin.getLogger().info(welcomeMessage);
    }

    public String getMessage(String path, String defaultValue) {
        return languageConfig.getString(path, defaultValue);
    }
}





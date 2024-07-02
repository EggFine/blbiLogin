package com.blbilink.blbilogin.modules;

import com.blbilink.blbilogin.BlbiLogin;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

public final class FileUtil {


    /**
     * Complete configuration(key and value, comments, etc)
     * @param resourceFile the resource file you want to complete
     */
    public static void completeFile(String resourceFile, String... notNeedSyncKeys) {
        Plugin plugin = BlbiLogin.plugin;
        plugin.getLogger().info("开始更新配置文件");
        if (plugin == null) {
            return;
        }

        InputStream stream = plugin.getResource(resourceFile);
        File file = new File(plugin.getDataFolder(), resourceFile);
        if (!file.exists()) {
            if (stream != null) {
                plugin.saveResource(resourceFile, false);
                return;
            }
            return;
        }
        if (stream == null) {
            plugin.getLogger().warning("File completion of '" + resourceFile + "' is failed.");
            return;
        }
        try {
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(reader);
            YamlConfiguration configuration2 = new YamlConfiguration();
            configuration2.load(file);

            for (String key : configuration.getKeys(true)) {
                Object value = configuration.get(key);
                if (value instanceof List<?>) {
                    List<?> list2 = configuration2.getList(key);
                    if (list2 == null) {
                        configuration2.set(key, value);
                        continue;
                    }
                }

                if (!configuration2.contains(key)) {
                    configuration2.set(key, value);
                }
                if (!configuration.getComments(key).equals(configuration2.getComments(key))) {
                    configuration2.setComments(key, configuration.getComments(key));
                }
                YamlConfigurationOptions options1 = configuration.options();
                YamlConfigurationOptions options2 = configuration2.options();

                if (!options2.getHeader().equals(options1.getHeader())) {
                    options2.setHeader(options1.getHeader());
                }
            }

            List<String> notSync = Arrays.stream(notNeedSyncKeys).toList();

            for (String key2 : configuration2.getKeys(true)) {
                boolean b = notSync.contains(key2);

                if (!configuration.contains(key2)) {
                    if (!b) {
                        configuration2.set(key2, null);
                    }
                }
            }

            configuration2.save(file);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning("File completion of '" + resourceFile + "' is failed.");
        }
    }

    /**
     * Complete language file (keys and values, comments, etc.)
     * FORCE SYNC
     * @param plugin plugin instance
     * @param resourceFile the language file you want to complete
     */
    public static void completeLangFile(Plugin plugin, String resourceFile){
        InputStream stream = plugin.getResource(resourceFile);
        File file = new File(plugin.getDataFolder() , resourceFile);

        if (!file.exists()) {
            if (stream != null) {
                plugin.saveResource(resourceFile, false);
                return;
            }
            return;
        }

        if (stream == null) {
            plugin.getLogger().warning("File completion of '" + resourceFile + "' is failed.");
            return;
        }

        try {
            Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(reader);

            YamlConfiguration configuration2 = new YamlConfiguration();
            configuration2.load(file);
            Set<String> keys = configuration.getKeys(true);
            for (String key : keys) {
                Object value = configuration.get(key);
                if (value instanceof List<?> list) {
                    List<?> list2 = configuration2.getList(key);
                    if (list2 == null || !(list.size() == list2.size())) {
                        configuration2.set(key, value);
                        continue;
                    }
                }
                if (!configuration2.contains(key)) {
                    configuration2.set(key, value);
                }
                if (!configuration.getComments(key).equals(configuration2.getComments(key))) {
                    configuration2.setComments(key, configuration.getComments(key));
                }
            }
            for (String key : configuration2.getKeys(true)) {
                if (configuration2.contains(key) & !configuration.contains(key)) {
                    configuration2.set(key, null);
                }
            }
            configuration2.save(file);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning("File completion of '" + resourceFile + "' is failed.");
        }
    }

    /**
     * Delete a directory
     * @param dirFile the directory
     * @return result
     */
    @CanIgnoreReturnValue
    public static boolean deleteDir(File dirFile){
        Callable<Boolean> callable = () -> {
            if (!dirFile.exists() || !dirFile.isDirectory() || dirFile.listFiles() == null) {
                return false;
            }
            boolean flag = true;

            File[] files = dirFile.listFiles();

            for (File file : Objects.requireNonNull(files)) {
                if (file.isFile()) {
                    flag = deleteFile(file);
                } else {
                    flag = deleteDir(file);
                }
                if (!flag) {
                    break;
                }
            }

            return flag && dirFile.delete();
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(callable);
        executorService.shutdown();

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete a file
     * @param file the file
     * @return result
     */
    @CanIgnoreReturnValue
    public static boolean deleteFile(File file){
        boolean flag = false;

        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }

        return flag;
    }
}
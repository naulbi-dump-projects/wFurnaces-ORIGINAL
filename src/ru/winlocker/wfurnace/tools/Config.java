package ru.winlocker.wfurnace.tools;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {

    /* Leaked by https://t.me/leak_mine
       Сурцы плагина были слиты телеграмм каналом https://t.me/leak_mine
    */	
	
    private static JavaPlugin instance = JavaPlugin.getProvidingPlugin(Config.class);

    public static FileConfiguration getFile(String fileName) {
        File file = new File(instance.getDataFolder(), fileName);
        if (!file.exists()) {
            instance.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void save(FileConfiguration config, String fileName) {
        try {
            config.save(new File(instance.getDataFolder(), fileName));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}


package de.realityrift.chunky.Main;

import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Listener.PlayerListener;
import de.realityrift.chunky.SQL.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static FileConfiguration config;
    static Language language;

    public void loadConfiguration() {
        File datafolder = this.getDataFolder();
        File configFile = new File(datafolder + File.separator + "config.yml");

        if (!configFile.exists()) {
            this.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public Main() {
        instance = this;
        language = new Language(new File(getDataFolder(), "lang.ini"));
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        loadConfiguration();
        checkAndCreateLanguageFile();
        MySQL.connect("chunkydb");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);

    }

    @Override
    public void onDisable() {
        MySQL.disconnect();
    }

    private void checkAndCreateLanguageFile() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File languageFile = new File(getDataFolder(), "language.ini");
        if (!languageFile.exists()) {
            getLogger().info("language.ini not found. Creating...");

            saveResource("language.ini", true);
        }
    }
}

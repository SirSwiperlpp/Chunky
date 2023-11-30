package de.realityrift.chunky.Main;

import de.realityrift.chunky.Commands.ChunkCMD;
import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Listener.BlockListener;
import de.realityrift.chunky.Listener.PlayerListener;
import de.realityrift.chunky.Listener.TNTListener;
import de.realityrift.chunky.Provider.ChunkProvider;
import de.realityrift.chunky.SQL.MySQL;
import de.realityrift.chunky.TabCompletter.ChunkTab;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static FileConfiguration config;
    public static Language language;

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
        try {
            ChunkProvider.createChunkdb();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new BlockListener(), this);
        pm.registerEvents(new TNTListener(), this);
        ChunkCMD chunkCMD = new ChunkCMD();
        getCommand("chunk").setExecutor(chunkCMD);
        getCommand("chunk").setTabCompleter(new ChunkTab(chunkCMD));

    }

    @Override
    public void onDisable() {
        MySQL.disconnect();
    }

    private void checkAndCreateLanguageFile() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File languageFile = new File(getDataFolder(), "lang.ini");
        if (!languageFile.exists()) {
            getLogger().info("language.ini not found. Creating...");

            saveResource("lang.ini", true);
        }
    }
}

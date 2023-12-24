package de.realityrift.chunky.Tasks;

import de.realityrift.chunky.API.EcoAPI;
import de.realityrift.chunky.API.ntpFetcher;
import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import de.realityrift.chunky.Provider.ChunkProvider;
import de.realityrift.chunky.Provider.EcoProvider;
import de.realityrift.chunky.SQL.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ChunkPaymentTask implements Runnable
{
    private final World world;

    public ChunkPaymentTask(World world) {
        this.world = world;
    }

    static Language language = new Language(new File(Main.getInstance().getDataFolder(), "lang.ini"));

    @Override
    public void run() {
        String timeToCheck = Main.config.getString("chunkrent.checks");
        ntpFetcher.run((rawDate, date, ex) -> {
            if (Objects.equals(timeToCheck, rawDate))
            {
                new BukkitRunnable()
                {
                    @Override
                    public void run() {
                        try {
                            String query = ("SELECT player_name,UUID, ChunkX, ChunkZ, world FROM claimed_chunks");
                            try (PreparedStatement stmt = MySQL.getConnection().prepareStatement(query); ResultSet rs = stmt.executeQuery(query)) {

                                while (rs.next()) {
                                    String pname = rs.getString("player_name");
                                    String uuid = rs.getString("UUID");
                                    String value1 = String.valueOf(rs.getInt("ChunkX"));
                                    String value2 = String.valueOf(rs.getInt("ChunkZ"));
                                    String value3 = rs.getString("world");

                                    if (Bukkit.getPlayer(pname) != null) {
                                        int currentMoney = EcoProvider.getPlayerMoney(uuid);

                                        if (currentMoney < 100) {
                                            World world = Bukkit.getWorld(value3);
                                            Chunk targetchunk = world.getChunkAt(Integer.parseInt(value1), Integer.parseInt(value2));
                                            ChunkProvider.removeChunk(targetchunk, value3);
                                            Bukkit.getPlayer(pname).sendMessage(language.get("prefix") + language.translateString("unclaim.notify.paymentfailed", value1, value2));
                                        } else {
                                            EcoAPI.removeMoney(uuid, 100);
                                        }
                                    } else {
                                        int currentMoney = EcoProvider.getPlayerMoney(uuid);

                                        if (currentMoney < 100) {
                                            World world = Bukkit.getWorld(value3);
                                            Chunk targetchunk = world.getChunkAt(Integer.parseInt(value1), Integer.parseInt(value2));
                                            ChunkProvider.removeChunk(targetchunk, value3);
                                        } else {
                                            EcoAPI.removeMoney(uuid, 100);
                                        }
                                    }

                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        /**
                        if (dataMap.isEmpty()) return;
                        System.out.println("Datamap not empty");
                        System.out.println("DataMap size: " + dataMap.size());

                        for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                            String uuid = entry.getKey();
                            String ChunkX = entry.getValue().get(0);
                            String ChunkZ = entry.getValue().get(1);

                            System.out.println("Processing for " + uuid);
                        }
                         **/
                    }
                }.runTaskAsynchronously(Main.getInstance());
            }
        });
    }
}

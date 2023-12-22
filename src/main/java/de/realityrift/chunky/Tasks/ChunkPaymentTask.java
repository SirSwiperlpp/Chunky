package de.realityrift.chunky.Tasks;

import de.realityrift.chunky.API.EcoAPI;
import de.realityrift.chunky.API.ntpFetcher;
import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import de.realityrift.chunky.Provider.ChunkProvider;
import de.realityrift.chunky.Provider.EcoProvider;
import de.realityrift.chunky.SQL.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChunkPaymentTask implements Runnable
{
    private final World world;

    public ChunkPaymentTask(World world) {
        this.world = world;
    }

    static Language language = new Language(new File(Main.getInstance().getDataFolder(), "lang.ini"));

    @Override
    public void run() {
        String timeToCheck = Main.config.getString("chunksrent.checks");
        //TODO: Work on this pls
        ntpFetcher.run((rawDate, date, ex) -> {
            if (timeToCheck == rawDate)
            {
                new BukkitRunnable()
                {
                    @Override
                    public void run() {
                        Map<String, List<String>> dataMap = new HashMap<>();

                        try {
                            String query = ("SELECT UUID, ChunkX, ChunkZ FROM claimed_chunks");
                            try (PreparedStatement stmt = MySQL.getConnection().prepareStatement(query); ResultSet rs = stmt.executeQuery(query)) {

                                while (rs.next()) {
                                    String key = rs.getString("UUID");
                                    String value1 = String.valueOf(rs.getInt("ChunkX"));
                                    String value2 = String.valueOf(rs.getInt("ChunkZ"));

                                    List<String> values = List.of(value1, value2);
                                    dataMap.put(key, values);
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        if (dataMap.isEmpty()) return;

                        for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                            String uuid = entry.getKey();
                            String ChunkX = entry.getValue().get(0);
                            String ChunkZ = entry.getValue().get(1);

                            if (Bukkit.getPlayer(uuid) != null) {
                                int currentMoney = EcoProvider.getPlayerMoney(uuid);

                                if (currentMoney > 100) {
                                    Bukkit.getPlayer(uuid).sendMessage(language.get("prefix") + language.translateString("unclaim.notify.paymentfailed", ChunkX, ChunkZ));
                                    return;
                                }

                                EcoAPI.removeMoney(uuid, 100);
                            } else {
                                int currentMoney = EcoProvider.getPlayerMoney(uuid);

                                if (currentMoney > 100) return;

                                EcoAPI.removeMoney(uuid, 100);
                            }
                        }
                    }
                }.runTaskAsynchronously(Main.getInstance());
            }
        });
    }
}

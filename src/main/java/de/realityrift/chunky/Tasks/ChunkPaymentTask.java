package de.realityrift.chunky.Tasks;

import de.realityrift.chunky.Provider.ChunkProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ChunkPaymentTask implements Runnable
{
    private final World world;

    public ChunkPaymentTask(World world) {
        this.world = world;
    }

    @Override
    public void run() {
        long wtime = world.getTime();

        if (wtime >= 17950 && wtime <= 18000) {
            if (ChunkProvider.getValueFromTheTalbe() != "check")
            {
                Bukkit.broadcastMessage("Es ist Mitternacht " + wtime);
                ChunkProvider.insertInTheTalbe();
            } else {
                Bukkit.getLogger().info("alrdychecked");
            }

        } else {
            Bukkit.getLogger().info("NotMidnight");
            ChunkProvider.removefromTheTalbe();
        }
    }
}

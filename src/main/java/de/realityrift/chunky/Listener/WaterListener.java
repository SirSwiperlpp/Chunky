package de.realityrift.chunky.Listener;

import de.realityrift.chunky.Main.Main;
import de.realityrift.chunky.Provider.ChunkProvider;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class WaterListener implements Listener {

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (Main.config.getBoolean("waterflows")) return;

        if (event.getBlock().isLiquid() && !event.getBlock().isBlockPowered()) {
            event.setCancelled(true);
        }
    }

}

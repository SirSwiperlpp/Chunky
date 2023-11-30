package de.realityrift.chunky.Listener;

import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import de.realityrift.chunky.Provider.ChunkProvider;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

import java.io.File;

public class TNTListener implements Listener {

    static Language language = new Language(new File(Main.getInstance().getDataFolder(), "lang.ini"));

    @EventHandler
    public void onTNTMinecartCreate(VehicleCreateEvent event) {
        if (event.getVehicle().getType() == EntityType.MINECART_TNT) {
            ExplosiveMinecart tntMinecart = (ExplosiveMinecart) event.getVehicle();
            Chunk tntChunk = tntMinecart.getLocation().getChunk();

            if (!ChunkProvider.getChunkFromdb(tntChunk)) {
                tntMinecart.remove();
            }
        }
    }

    @EventHandler
    public void onTNTMinecartDestroy(VehicleDestroyEvent event) {
        if (event.getVehicle().getType() == EntityType.MINECART_TNT) {
            ExplosiveMinecart tntMinecart = (ExplosiveMinecart) event.getVehicle();
            Chunk tntChunk = tntMinecart.getLocation().getChunk();

            if (!ChunkProvider.getChunkFromdb(tntChunk)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.TNTPrimed) {
            org.bukkit.entity.TNTPrimed tnt = (org.bukkit.entity.TNTPrimed) event.getEntity();
            Chunk tntChunk = event.getLocation().getChunk();

            if (ChunkProvider.getChunkFromdb(tntChunk)) {
                Player player = (Player) tnt.getSource();
                if (player != null && ChunkProvider.getPlayerNameForChunk(tntChunk).equals(player.getName())) {
                    return;
                }

                event.setCancelled(true);
                if (player != null) {
                    player.sendMessage(language.get("prefix") + language.get("not.trusted"));
                }
            }
        }
    }

    @EventHandler
    public void onTNTExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.TNTPrimed) {
            Chunk tntChunk = event.getLocation().getChunk();

            if (ChunkProvider.getChunkFromdb(tntChunk)) {
                event.blockList().removeIf(block -> !block.getChunk().equals(tntChunk));
            } else {
                event.setCancelled(true);
            }
        }
    }

}

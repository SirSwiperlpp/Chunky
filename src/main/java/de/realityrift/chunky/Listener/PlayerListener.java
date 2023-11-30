package de.realityrift.chunky.Listener;

import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import de.realityrift.chunky.Provider.ChunkProvider;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class PlayerListener implements Listener
{
    static Language language = new Language(new File(Main.getInstance().getDataFolder(), "lang.ini"));
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp())
        {
            event.setCancelled(false);
            return;
        }

        if (ChunkProvider.getChunkFromdb(event.getBlock().getChunk())) {
            String playerNameForChunk = ChunkProvider.getPlayerNameForChunk(event.getBlock().getChunk());

            if (Objects.equals(playerNameForChunk, event.getPlayer().getName())) {
                event.setCancelled(false);
            } else {
                event.getPlayer().sendMessage(language.get("prefix") + language.get("not.trusted"));
                event.setCancelled(true);
            }

            System.out.println("Chunk is in the database.");
            System.out.println("PlayerNameForChunk: " + playerNameForChunk);
        } else {
            System.out.println("Chunk is not in the database.");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp())
        {
            event.setCancelled(false);
            return;
        }

        if (ChunkProvider.getChunkFromdb(event.getBlock().getChunk())) {
            String playerNameForChunk = ChunkProvider.getPlayerNameForChunk(event.getBlock().getChunk());

            if (Objects.equals(playerNameForChunk, event.getPlayer().getName())) {
                event.setCancelled(false);
            } else {
                event.getPlayer().sendMessage(language.get("prefix") + language.get("not.trusted"));
                event.setCancelled(true);
            }

            System.out.println("Chunk is in the database.");
            System.out.println("PlayerNameForChunk: " + playerNameForChunk);
        } else {
            System.out.println("Chunk is not in the database.");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().isOp())
        {
            event.setCancelled(false);
            return;
        }

        if (event.getClickedBlock() != null && !canPlayerInteract(event.getClickedBlock(), event.getPlayer())) {
            event.getPlayer().sendMessage(language.get("prefix") + language.get("not.trusted"));
            event.setCancelled(true);
        }
    }


    private boolean canPlayerInteract(Block block, Player player) {
        Location blockLocation = block.getLocation();

        if (ChunkProvider.getChunkFromdb(blockLocation.getChunk())) {
            String playerNameForChunk = ChunkProvider.getPlayerNameForChunk(blockLocation.getChunk());

            return Objects.equals(playerNameForChunk, player.getName());
        }

        return true;
    }

}

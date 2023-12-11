package de.realityrift.chunky.Listener;

import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import de.realityrift.chunky.Provider.ChunkProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class ProtectionListener implements Listener
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
            Material[] savedblocks = {
                    Material.CHEST,
                    Material.FURNACE,
                    Material.SHULKER_BOX,
                    Material.CYAN_SHULKER_BOX,
                    Material.GRAY_SHULKER_BOX,
                    Material.BLUE_SHULKER_BOX,
                    Material.BLACK_SHULKER_BOX,
                    Material.BROWN_SHULKER_BOX,
                    Material.RED_SHULKER_BOX,
                    Material.ORANGE_SHULKER_BOX,
                    Material.WHITE_SHULKER_BOX,
                    Material.LIGHT_GRAY_SHULKER_BOX,
                    Material.YELLOW_SHULKER_BOX,
                    Material.LIME_SHULKER_BOX,
                    Material.GREEN_SHULKER_BOX,
                    Material.LIGHT_BLUE_SHULKER_BOX,
                    Material.PURPLE_SHULKER_BOX,
                    Material.PINK_SHULKER_BOX,
                    Material.MAGENTA_SHULKER_BOX,
                    Material.BLAST_FURNACE,
                    Material.SMOKER,
                    Material.BREWING_STAND,
                    Material.BEACON,
                    Material.CHISELED_BOOKSHELF,
                    Material.BARREL,
                    Material.DARK_OAK_DOOR,
                    Material.ACACIA_DOOR,
                    Material.BAMBOO_DOOR,
                    Material.CHERRY_DOOR,
                    Material.BIRCH_DOOR,
                    Material.CRIMSON_DOOR,
                    Material.MANGROVE_DOOR,
                    Material.OAK_DOOR,
                    Material.JUNGLE_DOOR,
                    Material.SPRUCE_DOOR,
                    Material.LEVER,
                    Material.REPEATER,
                    Material.COMPARATOR,
                    Material.OAK_TRAPDOOR,
                    Material.SPRUCE_TRAPDOOR,
                    Material.BIRCH_TRAPDOOR,
                    Material.JUNGLE_TRAPDOOR,
                    Material.ACACIA_TRAPDOOR,
                    Material.DARK_OAK_TRAPDOOR,
                    Material.MANGROVE_TRAPDOOR,
                    Material.CHERRY_TRAPDOOR,
                    Material.BAMBOO_TRAPDOOR,
                    Material.CRIMSON_TRAPDOOR,
                    Material.WARPED_TRAPDOOR,
                    Material.OAK_BUTTON,
                    Material.SPRUCE_BUTTON,
                    Material.BIRCH_BUTTON,
                    Material.JUNGLE_BUTTON,
                    Material.ACACIA_BUTTON,
                    Material.DARK_OAK_BUTTON,
                    Material.MANGROVE_BUTTON,
                    Material.CHERRY_BUTTON,
                    Material.BAMBOO_BUTTON,
                    Material.CRIMSON_BUTTON,
                    Material.WARPED_BUTTON,
                    Material.STONE_BUTTON,
                    Material.POLISHED_BLACKSTONE_BUTTON
            };

            Material clickedMaterial = event.getClickedBlock().getType();
            if (Arrays.asList(savedblocks).contains(clickedMaterial)) {
                event.getPlayer().sendMessage(language.get("prefix") + language.get("not.trusted"));
                event.setCancelled(true);
            }
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

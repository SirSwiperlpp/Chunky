package de.realityrift.chunky.Listener;

import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.io.File;

public class BlockListener implements Listener {

    static Language language = new Language(new File(Main.getInstance().getDataFolder(), "lang.ini"));

    @EventHandler
    public void onPistonPlace(BlockPlaceEvent event) {
        if (!Main.config.getBoolean("disable-pistons")) return;

        if (event.getBlockPlaced().getType() == Material.PISTON || event.getBlockPlaced().getType() == Material.STICKY_PISTON) {
            event.getPlayer().sendMessage(language.get("prefix") + language.translateString("block.disabled", String.valueOf(event.getBlockPlaced().getType())));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrystalCraft(CraftItemEvent event) {
        if (!Main.config.getBoolean("disable-crystal")) return;

        if (event.getRecipe() != null && event.getRecipe().getResult().getType() == Material.END_CRYSTAL) {
            event.getWhoClicked().sendMessage(language.get("prefix") + language.translateString("block.disabled", String.valueOf(event.getRecipe().getResult().getType())));
            event.setCancelled(true);
        }
    }

}

package de.realityrift.chunky.Listener;

import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

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

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack().getType() == Material.END_CRYSTAL) {
            Player player = event.getPlayer();

            player.getInventory().addItem(
                    new ItemStack(Material.GLASS, 9),
                    new ItemStack(Material.ENDER_EYE, 1),
                    new ItemStack(Material.GHAST_TEAR, 1)
            );

            event.getPlayer().getInventory().remove(Material.END_CRYSTAL);

            player.sendMessage(language.get("prefix") + language.translateString("block.disabled", String.valueOf(event.getItem().getItemStack().getType())));
        }
    }

    @EventHandler
    public void onEndCrystalDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.END_CRYSTAL)
        {
            event.getPlayer().sendMessage(language.get("prefix") + language.translateString("block.disabled", String.valueOf(event.getItemDrop().getItemStack().getType())));
            event.setCancelled(true);
        }
    }

}

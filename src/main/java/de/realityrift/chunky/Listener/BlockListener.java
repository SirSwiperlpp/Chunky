package de.realityrift.chunky.Listener;

import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
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
    public void onCrystalDamage(EntityDamageEvent event)
    {
        if (!Main.config.getBoolean("disable-crystal")) return;
        Entity entity = event.getEntity();

        if (entity.equals(EntityType.ENDER_CRYSTAL))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrystalExplode(EntityExplodeEvent event)
    {
        if (!Main.config.getBoolean("disable-crystal")) return;
        Entity entity = event.getEntity();

        if (entity.getType().equals(EntityType.ENDER_CRYSTAL))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAnchorPlace(BlockPlaceEvent event)
    {
        if (!Main.config.getBoolean("disable-anchor")) return;

        if (event.getBlockPlaced().getType().equals(Material.RESPAWN_ANCHOR))
        {
            event.getPlayer().sendMessage(language.get("prefix") + language.translateString("block.disabled", String.valueOf(event.getBlockPlaced().getType())));
            event.setCancelled(true);
        }
    }

}

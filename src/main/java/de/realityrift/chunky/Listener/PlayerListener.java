package de.realityrift.chunky.Listener;

import de.realityrift.chunky.Provider.EcoProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerListener implements Listener
{
    //TODO: delete this whole listener if its still here. its just for test Purpose
    @EventHandler
    public void PlayerJoinListener(PlayerJoinEvent event) throws SQLException {
        if (EcoProvider.checkForPlayer(event.getPlayer()).equals(""))
        {
            EcoProvider.insertPlayer(event.getPlayer());
            return;
        }
    }
}

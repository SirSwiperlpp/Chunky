package de.realityrift.chunky.Commands;

import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import de.realityrift.chunky.Provider.ChunkProvider;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

public class ChunkCMD implements CommandExecutor {

    static Language language = new Language(new File(Main.getInstance().getDataFolder(), "lang.ini"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
    {
        if (!(sender instanceof Player))
        {
            Bukkit.getLogger().warning(language.get("console.sender"));
            return true;
        }

        if (strings.length < 1)
        {
            String usage = "/chunk [claim | unclaim | trust]";
            sender.sendMessage(language.get("prefix") + language.translateString("usage.command", usage));
            return true;
        }

        switch (strings[0].toLowerCase())
        {
            case "claim":
                Player player = (Player) sender;
                Chunk chunk = player.getLocation().getChunk();

                System.out.println("Checking chunk claim status...");

                if (Objects.equals(ChunkProvider.getPlayerNameForChunk(chunk), "")) {
                    System.out.println("Chunk is not claimed. Claiming now...");
                    ChunkProvider.insertChunk(player, chunk);
                    player.sendMessage(language.get("prefix") + language.get("claim.success"));
                } else {
                    String alrdyclaimed = ChunkProvider.getPlayerNameForChunk(chunk);
                    System.out.println("Chunk is already claimed by: " + alrdyclaimed);
                    player.sendMessage(language.get("prefix") + language.get("claim.failed.claimed"));
                }

                break;

            case "unclaim":
                Player player2 = (Player) sender;
                Chunk chunk2 = player2.getLocation().getChunk();

                break;

            case "trust":
                sender.sendMessage("wip");
                break;
        }


        return false;
    }
}

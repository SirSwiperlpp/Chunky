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
        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();

        switch (strings[0].toLowerCase())
        {
            case "claim":
                System.out.println("Checking chunk claim status...");

                if (Objects.equals(ChunkProvider.getPlayerNameForChunk(chunk), "")) {
                    System.out.println("Chunk is not claimed. Claiming now...");
                    ChunkProvider.insertChunk(player, chunk);
                    player.sendMessage(language.get("prefix") + language.get("claim.success"));
                } else {
                    String alrdyclaimed = ChunkProvider.getPlayerNameForChunk(chunk);
                    System.out.println("Chunk is already claimed by: " + alrdyclaimed);
                    player.sendMessage(language.get("prefix") + language.translateString("claim.failed.claimed", alrdyclaimed));
                }

                break;

            case "unclaim":
                if (!ChunkProvider.getChunkFromdb(chunk))
                {
                    player.sendMessage(language.get("prefix") + language.get("chunk.not.claimed"));
                    return true;
                }

                if (player.isOp())
                {
                    String target = ChunkProvider.getPlayerNameForChunk(chunk);
                    ChunkProvider.removeChunk(chunk);
                    player.sendMessage(language.get("prefix") + language.get("unclaim.success"));

                    if (Objects.equals(target, player.getName())) return true;

                    if (Main.config.getBoolean("notifyunclaim"))
                    {
                        String chunkX = String.valueOf(chunk.getX());
                        String chunkZ = String.valueOf(chunk.getZ());
                        Bukkit.getPlayer(target).sendMessage(language.get("prefix") + language.translateString("unclaim.notify", chunkX, chunkZ));
                        return true;
                    }
                    return true;
                }

                if (Objects.equals(ChunkProvider.getPlayerNameForChunk(chunk), player.getName()))
                {
                    ChunkProvider.removeChunk(chunk);
                    player.sendMessage(language.get("prefix") + language.get("unclaim.success"));
                    return true;
                } else {
                    player.sendMessage(language.get("prefix") + language.get("unclaim.failed.not.owned"));
                }
                break;

            case "trust":
                sender.sendMessage("wip");
                break;

            case "info":
                sender.sendMessage("wip");
                break;

            default:
                String usage = "/chunk [claim | unclaim | trust | info]";
                player.sendMessage(language.get("prefix") + language.translateString("usage.command", usage));
                break;
        }


        return false;
    }
}

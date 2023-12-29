package de.realityrift.chunky.Commands;

import de.realityrift.chunky.API.EcoAPI;
import de.realityrift.chunky.Lang.Language;
import de.realityrift.chunky.Main.Main;
import de.realityrift.chunky.Provider.ChunkProvider;
import de.realityrift.chunky.Provider.EcoProvider;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Map;
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
            String usage = "§c/chunk [claim | unclaim | trust | untrust | flag | info]";
            sender.sendMessage(language.get("prefix") + language.translateString("usage.command", usage));
            return true;
        }
        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();

        switch (strings[0].toLowerCase())
        {
            case "claim":
                if (!Main.config.getBoolean("enabledCommands.claim"))
                {
                    player.sendMessage(language.get("prefix") + language.get("feature.disabled"));
                    return true;
                }

                if (Main.config.getInt("chunkrent.maxclaimsperplayer") <= ChunkProvider.countPlayerChunks(player.getName()))
                {
                    player.sendMessage(language.get("prefix") + language.get("claim.failed.maxclaims"));
                    return true;
                }

                if (Main.config.getBoolean("chunkrent.paychunks"))
                {
                    if (EcoProvider.getPlayerMoney(String.valueOf(player.getUniqueId())) < 100)
                    {
                        player.sendMessage(language.get("prefix") + language.get("claim.failed.notmoney"));
                        return true;
                    }
                }

                if (Objects.equals(ChunkProvider.getPlayerNameForChunk(chunk, chunk.getWorld().getName()), "")) {
                    String trusted = "None";
                    String flags = "None";
                    ChunkProvider.insertChunk(player, chunk, trusted, flags);
                    if (Main.config.getBoolean("chunkrent.paychunks"))
                    {
                        EcoAPI.removeMoney(String.valueOf(player.getUniqueId()), 100);
                    }
                    player.sendMessage(language.get("prefix") + language.get("claim.success"));
                } else {
                    String alrdyclaimed = ChunkProvider.getPlayerNameForChunk(chunk, chunk.getWorld().getName());
                    player.sendMessage(language.get("prefix") + language.translateString("claim.failed.claimed", alrdyclaimed));
                }

                break;

            case "unclaim":
                if (!Main.config.getBoolean("enabledCommands.unclaim"))
                {
                    player.sendMessage(language.get("prefix") + language.get("feature.disabled"));
                    return true;
                }

                if (!ChunkProvider.getChunkFromdb(chunk, player.getWorld().getName()))
                {
                    player.sendMessage(language.get("prefix") + language.get("chunk.not.claimed"));
                    return true;
                }

                if (player.isOp())
                {
                    String target = ChunkProvider.getPlayerNameForChunk(chunk, chunk.getWorld().getName());
                    String world = player.getWorld().getName();
                    ChunkProvider.removeChunk(chunk, world);
                    player.sendMessage(language.get("prefix") + language.get("unclaim.success"));

                    if (Objects.equals(target, player.getName())) return true;

                    if (Bukkit.getPlayer(target) == null) return true;

                    if (Main.config.getBoolean("notifyunclaim"))
                    {
                        String chunkX = String.valueOf(chunk.getX());
                        String chunkZ = String.valueOf(chunk.getZ());
                        Bukkit.getPlayer(target).sendMessage(language.get("prefix") + language.translateString("unclaim.notify", chunkX, chunkZ));
                        return true;
                    }
                    return true;
                }

                if (Objects.equals(ChunkProvider.getPlayerNameForChunk(chunk, chunk.getWorld().getName()), player.getName()))
                {
                    String world = player.getWorld().getName();
                    ChunkProvider.removeChunk(chunk, world);
                    player.sendMessage(language.get("prefix") + language.get("unclaim.success"));
                    return true;
                } else {
                    player.sendMessage(language.get("prefix") + language.get("unclaim.failed.not.owned"));
                }
                break;

            case "trust":
                if (!Main.config.getBoolean("enabledCommands.trust"))
                {
                    player.sendMessage(language.get("prefix") + language.get("feature.disabled"));
                    return true;
                }

                if (strings.length < 2)
                {
                    String usage = "§c/chunk [trust] [playername]";
                    sender.sendMessage(language.get("prefix") + language.translateString("usage.command", usage));
                    return true;
                }

                if (ChunkProvider.getPlayerNameForChunk(player.getLocation().getChunk(), player.getWorld().getName()).equals(player.getName()))
                {
                    String newtrustraw = strings[1];

                    if (Bukkit.getPlayer(newtrustraw) == null)
                    {
                        player.sendMessage(language.get("prefix") + language.get("trust.failed.playernotonline"));
                        return true;
                    }

                    if (newtrustraw.equals(player.getName()))
                    {
                        player.sendMessage(language.get("prefix") + language.get("trust.failed.self"));
                        return true;
                    }

                    Player newtrust = Bukkit.getPlayer(newtrustraw);
                    String ctrusted = ChunkProvider.getTrusted(player, player.getLocation().getChunk());

                    if (ctrusted.contains(newtrust.getName()))
                    {
                        player.sendMessage(language.get("prefix") + language.translateString("trust.failed.alrdy", newtrust.getName()));
                        return true;
                    }

                    String newTrustedValue;
                    if (ctrusted.equals("None")) {
                        newTrustedValue = newtrust.getName();
                    } else {
                        newTrustedValue = ctrusted + "," + newtrust;
                    }
                    ChunkProvider.addTruted(newTrustedValue, player);
                    player.sendMessage(language.get("prefix") + language.translateString("trust.success", newtrust.getName()));

                    if (Main.config.getBoolean("notifytrust"))
                    {
                        newtrust.sendMessage(language.get("prefix") + language.translateString("trust.notify.trusted", player.getName()));
                    }
                } else {
                    player.sendMessage(language.get("prefix") + language.get("trust.failed"));
                    return true;
                }
                break;

            case "untrust":
                break;

            case "info":
                if (!Main.config.getBoolean("enabledCommands.info"))
                {
                    player.sendMessage(language.get("prefix") + language.get("feature.disabled"));
                    return true;
                }
                Map<String, Object> ChunkData = ChunkProvider.getAllInfosAboutChunk(chunk, player.getWorld().getName());

                if (ChunkData.isEmpty())
                {
                    player.sendMessage(language.get("prefix") + language.get("chunk.not.claimed"));
                    return true;
                }
                String pname = String.valueOf(ChunkData.get("player_name"));
                String ctrusted = String.valueOf(ChunkData.get("trusted"));
                String cflags = String.valueOf(ChunkData.get("flags"));
                player.sendMessage("§7------" + language.get("prefix") + "§7------\n§6Chunk Owner §8> §c" + pname + "\n§6Trusted §8> §c" + ctrusted + "\n§6Flags: §8> §c" + cflags + "\n§7------" + language.get("prefix") + "§7------");
                break;

            case "flag":
                if (!Main.config.getBoolean("enabledCommands.flag"))
                {
                    player.sendMessage(language.get("prefix") + language.get("feature.disabled"));
                    return true;
                }

                sender.sendMessage(language.get("prefix") + language.get("feature.wip"));
                break;

            case "settype":
                if (!Main.config.getBoolean("enabledCommands.settype"))
                {
                    player.sendMessage(language.get("prefix") + language.get("feature.disabled"));
                    return true;
                }

                sender.sendMessage(language.get("prefix") + language.get("feature.wip"));
                break;

            case "lock":
                if (!Main.config.getBoolean("enabledCommands.lock"))
                {
                    player.sendMessage(language.get("prefix") + language.get("feature.disabled"));
                    return true;
                }

                sender.sendMessage(language.get("prefix") + language.get("feature.wip"));
                break;

            default:
                String usage = "§c/chunk [claim | unclaim | trust | flag | info]";
                player.sendMessage(language.get("prefix") + language.translateString("usage.command", usage));
                break;
        }


        return false;
    }
}

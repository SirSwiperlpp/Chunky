package de.realityrift.chunky.TabCompletter;

import de.realityrift.chunky.Commands.ChunkCMD;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChunkTab implements TabCompleter {

    private final ChunkCMD chunkCMD;

    public ChunkTab(ChunkCMD chunkCMD) {
        this.chunkCMD = chunkCMD;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (sender.isOp()) {
            if (args.length == 1) {
                List<String> subCommands = Arrays.asList("claim", "unclaim", "trust", "untrust", "info", "flag", "settype", "lock");
                StringUtil.copyPartialMatches(args[0], subCommands, completions);
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("trust")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.getName().equalsIgnoreCase(sender.getName())) {
                        completions.add(player.getName());
                    }
                }
            }

            Collections.sort(completions);
            return completions;
        }

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("claim", "unclaim", "trust", "untrust", "info", "flag");
            StringUtil.copyPartialMatches(args[0], subCommands, completions);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("trust")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getName().equalsIgnoreCase(sender.getName())) {
                    completions.add(player.getName());
                }
            }
        }

        Collections.sort(completions);
        return completions;
    }
}

package de.realityrift.chunky.TabCompletter;

import de.realityrift.chunky.Commands.ChunkCMD;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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

        if (sender.isOp())
        {
            if (args.length == 1) {
                List<String> subCommands = Arrays.asList("claim", "unclaim", "trust", "info", "flag", "settype", "lock");
                StringUtil.copyPartialMatches(args[0], subCommands, completions);
            }

            Collections.sort(completions);
            return completions;
        }

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("claim", "unclaim", "trust", "info", "flag");
            StringUtil.copyPartialMatches(args[0], subCommands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}

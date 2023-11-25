package de.realityrift.chunky.TabCompletter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ChunkTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length < 1) {
            String partialCommand = args[0].toLowerCase();

            switch (partialCommand) {
                case "claim":
                    completions.add("claim");
                    break;
                case "unclaim":
                    completions.add("unclaim");
                    break;
                case "trust":
                    completions.add("trust");
                    break;
            }
        }

        return completions;
    }
}

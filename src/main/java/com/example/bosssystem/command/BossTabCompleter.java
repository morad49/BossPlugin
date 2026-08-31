package com.example.bosssystem.command;

import com.example.bosssystem.BossSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BossTabCompleter implements TabCompleter {

    private final BossSystem plugin;

    public BossTabCompleter(BossSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("spawn", "kill", "setspawn", "wand", "setregion", "give", "reload", "help"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("spawn")) {
                completions.addAll(Arrays.asList("1", "2", "3"));
            } else if (args[0].equalsIgnoreCase("give")) {
                if (plugin.getConfigManager().getConfig().getConfigurationSection("items") != null) {
                    completions.addAll(plugin.getConfigManager().getConfig().getConfigurationSection("items").getKeys(false));
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            for (Player p : Bukkit.getOnlinePlayers()) completions.add(p.getName());
        }

        return completions;
    }
}

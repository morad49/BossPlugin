package com.example.bosssystem.command;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossLevel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BossCommand implements CommandExecutor {

    private final BossSystem plugin;

    public BossCommand(BossSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(plugin.getMessageManager().colorize("&8=== &c&lBossSystem Help &8==="));
            sender.sendMessage(plugin.getMessageManager().colorize("&e/boss spawn <1|2|3> &7- Spawn a boss"));
            sender.sendMessage(plugin.getMessageManager().colorize("&e/boss kill &7- Kill current active boss"));
            sender.sendMessage(plugin.getMessageManager().colorize("&e/boss setspawn &7- Set arena spawn point"));
            sender.sendMessage(plugin.getMessageManager().colorize("&e/boss wand &7- Give arena region selection wand"));
            sender.sendMessage(plugin.getMessageManager().colorize("&e/boss setregion &7- Save selected arena region"));
            sender.sendMessage(plugin.getMessageManager().colorize("&e/boss give <item> [player] [amount] &7- Give custom boss item"));
            sender.sendMessage(plugin.getMessageManager().colorize("&e/boss reload &7- Reload configurations"));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "spawn" -> {
                if (!sender.hasPermission("boss.spawn")) return noPerm(sender);
                if (args.length < 2) {
                    sender.sendMessage(plugin.getMessageManager().getFormattedMessage("invalid-level"));
                    return true;
                }
                try {
                    int lvlInt = Integer.parseInt(args[1]);
                    BossLevel level = BossLevel.fromInt(lvlInt);
                    if (level == null) {
                        sender.sendMessage(plugin.getMessageManager().getFormattedMessage("invalid-level"));
                        return true;
                    }
                    boolean success = plugin.getBossManager().spawnBoss(level);
                    if (!success) {
                        sender.sendMessage(plugin.getMessageManager().getFormattedMessage("already-active"));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(plugin.getMessageManager().getFormattedMessage("invalid-level"));
                }
            }
            case "kill" -> {
                if (!sender.hasPermission("boss.admin")) return noPerm(sender);
                if (plugin.getBossManager().getActiveBoss() == null) {
                    sender.sendMessage(plugin.getMessageManager().getFormattedMessage("no-active-boss"));
                } else {
                    plugin.getBossManager().purgeActiveBoss();
                    sender.sendMessage(plugin.getMessageManager().colorize("&aActive boss purged."));
                }
            }
            case "setspawn" -> {
                if (!(sender instanceof Player player)) return true;
                if (!player.hasPermission("boss.admin")) return noPerm(player);
                plugin.getConfigManager().setSpawnLocation(player.getLocation());
                player.sendMessage(plugin.getMessageManager().getFormattedMessage("boss-spawn-set"));
            }
            case "wand" -> {
                if (!(sender instanceof Player player)) return true;
                if (!player.hasPermission("boss.region")) return noPerm(player);
                ItemStack wand = new ItemStack(Material.BLAZE_ROD);
                ItemMeta meta = wand.getItemMeta();
                meta.setDisplayName(plugin.getMessageManager().colorize("&cBoss Selection Wand"));
                wand.setItemMeta(meta);
                player.getInventory().addItem(wand);
                player.sendMessage(plugin.getMessageManager().getFormattedMessage("boss-wand-given"));
            }
            case "setregion" -> {
                if (!(sender instanceof Player player)) return true;
                if (!player.hasPermission("boss.region")) return noPerm(player);
                boolean saved = plugin.getRegionManager().saveSelectionAsRegion(player.getUniqueId());
                if (saved) {
                    player.sendMessage(plugin.getMessageManager().getFormattedMessage("boss-region-set"));
                } else {
                    player.sendMessage(plugin.getMessageManager().colorize("&cPlease select Pos1 and Pos2 using /boss wand first."));
                }
            }
            case "give" -> {
                if (!sender.hasPermission("boss.item")) return noPerm(sender);
                if (args.length < 2) {
                    sender.sendMessage(plugin.getMessageManager().getFormattedMessage("invalid-item"));
                    return true;
                }
                String itemId = args[1];
                Player target = (sender instanceof Player p) ? p : null;
                if (args.length >= 3) target = Bukkit.getPlayer(args[2]);
                int amount = 1;
                if (args.length >= 4) {
                    try { amount = Integer.parseInt(args[3]); } catch (NumberFormatException ignored) {}
                }

                if (target == null) {
                    sender.sendMessage(plugin.getMessageManager().colorize("&cTarget player not found."));
                    return true;
                }

                ItemStack stack = plugin.getItemManager().createCustomItem(itemId, amount);
                if (stack == null) {
                    sender.sendMessage(plugin.getMessageManager().getFormattedMessage("invalid-item"));
                    return true;
                }
                target.getInventory().addItem(stack);
                sender.sendMessage(plugin.getMessageManager().colorize("&aGave " + amount + "x " + itemId + " to " + target.getName()));
            }
            case "reload" -> {
                if (!sender.hasPermission("boss.reload")) return noPerm(sender);
                plugin.getConfigManager().loadConfig();
                plugin.getMessageManager().loadMessages();
                plugin.getRegionManager().loadRegion();
                sender.sendMessage(plugin.getMessageManager().getFormattedMessage("reload-success"));
            }
        }
        return true;
    }

    private boolean noPerm(CommandSender sender) {
        sender.sendMessage(plugin.getMessageManager().getFormattedMessage("no-permission"));
        return true;
    }
}

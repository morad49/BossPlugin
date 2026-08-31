package com.example.bosssystem.reward;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossLevel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BossRewardManager {

    private final BossSystem plugin;

    public BossRewardManager(BossSystem plugin) {
        this.plugin = plugin;
    }

    public void giveRewards(Player killer, BossLevel level) {
        List<Map<?, ?>> rewardList = plugin.getConfigManager().getConfig().getMapList("rewards." + level.getConfigKey());
        if (rewardList.isEmpty()) return;

        for (Map<?, ?> entry : rewardList) {
            double chance = entry.containsKey("chance") ? ((Number) entry.get("chance")).doubleValue() : 100.0;
            double roll = ThreadLocalRandom.current().nextDouble(0.0, 100.0);

            if (roll <= chance) {
                String type = (String) entry.get("type");
                if ("ITEM".equalsIgnoreCase(type)) {
                    String itemKey = (String) entry.get("item");
                    int amount = entry.containsKey("amount") ? ((Number) entry.get("amount")).intValue() : 1;
                    ItemStack item = plugin.getItemManager().createCustomItem(itemKey, amount);
                    if (item != null) killer.getInventory().addItem(item);
                } else if ("COMMAND".equalsIgnoreCase(type)) {
                    String cmd = (String) entry.get("command");
                    cmd = cmd.replace("%player%", killer.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            }
        }
        killer.sendMessage(plugin.getMessageManager().getFormattedMessage("boss-reward"));
    }
}

package com.example.bosssystem.reward;

import com.example.bosssystem.BossSystem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BossItemManager {

    private final BossSystem plugin;
    private final NamespacedKey itemKey;

    public BossItemManager(BossSystem plugin) {
        this.plugin = plugin;
        this.itemKey = new NamespacedKey(plugin, "boss_item_id");
    }

    public ItemStack createCustomItem(String identifier, int amount) {
        FileConfiguration config = plugin.getConfigManager().getConfig();
        String path = "items." + identifier + ".";

        if (!config.contains("items." + identifier)) return null;

        String matStr = config.getString(path + "material", "STONE");
        String name = plugin.getMessageManager().colorize(config.getString(path + "name", "&aItem"));
        List<String> rawLore = config.getStringList(path + "lore");
        int customModelData = config.getInt(path + "custom-model-data", 0);

        ItemStack stack = new ItemStack(Material.valueOf(matStr.toUpperCase()), amount);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> lore = new ArrayList<>();
            for (String line : rawLore) lore.add(plugin.getMessageManager().colorize(line));
            meta.setLore(lore);
            if (customModelData > 0) meta.setCustomModelData(customModelData);

            // PDC Storage for 100% accurate identification
            meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, identifier);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    public boolean isCustomItem(ItemStack stack, String identifier) {
        if (stack == null || !stack.hasItemMeta()) return false;
        ItemMeta meta = stack.getItemMeta();
        String id = meta.getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
        return identifier.equals(id);
    }
}

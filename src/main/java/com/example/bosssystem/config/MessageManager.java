package com.example.bosssystem.config;

import com.example.bosssystem.BossSystem;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class MessageManager {

    private final BossSystem plugin;
    private FileConfiguration messagesConfig;

    public MessageManager(BossSystem plugin) {
        this.plugin = plugin;
    }

    public void loadMessages() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(file);
    }

    public String getRawMessage(String key) {
        return messagesConfig.getString(key, "&cMissing message: " + key);
    }

    public String getFormattedMessage(String key) {
        String prefix = messagesConfig.getString("prefix", "");
        String msg = messagesConfig.getString(key, "");
        return colorize(prefix + msg);
    }

    public String colorize(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void sendTitle(Player player, String keyPath, String bossName) {
        String title = colorize(messagesConfig.getString(keyPath + ".title", "").replace("%boss%", bossName));
        String subtitle = colorize(messagesConfig.getString(keyPath + ".subtitle", "").replace("%boss%", bossName));
        int fadeIn = messagesConfig.getInt(keyPath + ".fade-in", 10);
        int stay = messagesConfig.getInt(keyPath + ".stay", 60);
        int fadeOut = messagesConfig.getInt(keyPath + ".fade-out", 20);

        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}

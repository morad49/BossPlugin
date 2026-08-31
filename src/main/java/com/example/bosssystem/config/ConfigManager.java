package com.example.bosssystem.config;

import com.example.bosssystem.BossSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final BossSystem plugin;
    private FileConfiguration config;

    public ConfigManager(BossSystem plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public int getSpawnInterval() {
        return config.getInt("boss.spawn-interval", 3600);
    }

    public boolean isAutoSpawnEnabled() {
        return config.getBoolean("boss.auto-spawn-enabled", true);
    }

    public Location getSpawnLocation() {
        String worldName = config.getString("spawn-location.world", "");
        if (worldName == null || worldName.isEmpty()) return null;
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        double x = config.getDouble("spawn-location.x");
        double y = config.getDouble("spawn-location.y");
        double z = config.getDouble("spawn-location.z");
        float yaw = (float) config.getDouble("spawn-location.yaw");
        float pitch = (float) config.getDouble("spawn-location.pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public void setSpawnLocation(Location loc) {
        config.set("spawn-location.world", loc.getWorld().getName());
        config.set("spawn-location.x", loc.getX());
        config.set("spawn-location.y", loc.getY());
        config.set("spawn-location.z", loc.getZ());
        config.set("spawn-location.yaw", loc.getYaw());
        config.set("spawn-location.pitch", loc.getPitch());
        plugin.saveConfig();
    }
}

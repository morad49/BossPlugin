package com.example.bosssystem.region;

import com.example.bosssystem.BossSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossRegionManager {

    private final BossSystem plugin;
    private BossRegion region;
    private final Map<UUID, Location[]> selections = new HashMap<>();

    public BossRegionManager(BossSystem plugin) {
        this.plugin = plugin;
        loadRegion();
    }

    public void loadRegion() {
        FileConfiguration config = plugin.getConfigManager().getConfig();
        String worldName = config.getString("region.world", "");
        if (worldName.isEmpty()) return;
        World w = Bukkit.getWorld(worldName);
        if (w == null) return;

        int minX = config.getInt("region.min-x");
        int minY = config.getInt("region.min-y");
        int minZ = config.getInt("region.min-z");
        int maxX = config.getInt("region.max-x");
        int maxY = config.getInt("region.max-y");
        int maxZ = config.getInt("region.max-z");

        this.region = new BossRegion(w, minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void setPos1(UUID uuid, Location loc) {
        selections.computeIfAbsent(uuid, k -> new Location[2])[0] = loc;
    }

    public void setPos2(UUID uuid, Location loc) {
        selections.computeIfAbsent(uuid, k -> new Location[2])[1] = loc;
    }

    public boolean saveSelectionAsRegion(UUID uuid) {
        Location[] locs = selections.get(uuid);
        if (locs == null || locs[0] == null || locs[1] == null) return false;

        World w = locs[0].getWorld();
        int minX = Math.min(locs[0].getBlockX(), locs[1].getBlockX());
        int minY = Math.min(locs[0].getBlockY(), locs[1].getBlockY());
        int minZ = Math.min(locs[0].getBlockZ(), locs[1].getBlockZ());
        int maxX = Math.max(locs[0].getBlockX(), locs[1].getBlockX());
        int maxY = Math.max(locs[0].getBlockY(), locs[1].getBlockY());
        int maxZ = Math.max(locs[0].getBlockZ(), locs[1].getBlockZ());

        FileConfiguration config = plugin.getConfigManager().getConfig();
        config.set("region.world", w.getName());
        config.set("region.min-x", minX);
        config.set("region.min-y", minY);
        config.set("region.min-z", minZ);
        config.set("region.max-x", maxX);
        config.set("region.max-y", maxY);
        config.set("region.max-z", maxZ);
        plugin.saveConfig();

        this.region = new BossRegion(w, minX, minY, minZ, maxX, maxY, maxZ);
        return true;
    }

    public BossRegion getRegion() {
        return region;
    }
}

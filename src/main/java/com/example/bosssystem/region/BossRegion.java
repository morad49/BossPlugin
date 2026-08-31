package com.example.bosssystem.region;

import org.bukkit.Location;
import org.bukkit.World;

public class BossRegion {

    private final World world;
    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;

    public BossRegion(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.world = world;
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public boolean isComplete() {
        return world != null && (minX != 0 || maxX != 0);
    }

    public boolean contains(Location loc) {
        if (loc == null || loc.getWorld() == null || !loc.getWorld().equals(world)) return false;
        return loc.getBlockX() >= minX && loc.getBlockX() <= maxX
                && loc.getBlockY() >= minY && loc.getBlockY() <= maxY
                && loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ;
    }

    public Location clamp(Location loc) {
        if (loc == null || loc.getWorld() == null) return loc;
        double clampedX = Math.max(minX + 0.5, Math.min(maxX + 0.5, loc.getX()));
        double clampedY = Math.max(minY + 0.5, Math.min(maxY + 0.5, loc.getY()));
        double clampedZ = Math.max(minZ + 0.5, Math.min(maxZ + 0.5, loc.getZ()));
        return new Location(world, clampedX, clampedY, clampedZ, loc.getYaw(), loc.getPitch());
    }
}

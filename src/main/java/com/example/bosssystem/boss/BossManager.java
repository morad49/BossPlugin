package com.example.bosssystem.boss;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.animation.BossSpawnAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

public class BossManager {

    private final BossSystem plugin;
    private BossInstance activeBoss;
    private BukkitTask tickerTask;
    private BukkitTask autoSpawnTask;

    public BossManager(BossSystem plugin) {
        this.plugin = plugin;
        startTicker();
    }

    private void startTicker() {
        this.tickerTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (activeBoss != null) {
                if (!activeBoss.getEntity().isValid() || activeBoss.getEntity().isDead()) {
                    purgeActiveBoss();
                } else {
                    activeBoss.tick();
                }
            }
        }, 10L, 10L); // Ticks every 0.5s for optimal MSPT performance
    }

    public void startAutoSpawnTask() {
        if (!plugin.getConfigManager().isAutoSpawnEnabled()) return;
        long intervalTicks = plugin.getConfigManager().getSpawnInterval() * 20L;
        this.autoSpawnTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (activeBoss == null) {
                spawnBoss(BossLevel.LEVEL_1);
            }
        }, intervalTicks, intervalTicks);
    }

    public void stopAutoSpawnTask() {
        if (autoSpawnTask != null) autoSpawnTask.cancel();
    }

    public boolean spawnBoss(BossLevel level) {
        if (activeBoss != null) return false;

        Location spawnLoc = plugin.getConfigManager().getSpawnLocation();
        if (spawnLoc == null) return false;

        BossSpawnAnimation animation = new BossSpawnAnimation(plugin, spawnLoc, level, (bossInst) -> {
            this.activeBoss = bossInst;
        });
        animation.start();
        return true;
    }

    public void purgeActiveBoss() {
        if (activeBoss != null) {
            activeBoss.remove();
            activeBoss = null;
        }
    }

    public BossInstance getActiveBoss() {
        return activeBoss;
    }
}

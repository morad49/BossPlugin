package com.example.bosssystem.animation;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import com.example.bosssystem.boss.BossLevel;
import com.example.bosssystem.util.ParticleUtil;
import com.example.bosssystem.util.SoundUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class BossSpawnAnimation {

    private final BossSystem plugin;
    private final Location targetLoc;
    private final BossLevel level;
    private final Consumer<BossInstance> onComplete;

    public BossSpawnAnimation(BossSystem plugin, Location targetLoc, BossLevel level, Consumer<BossInstance> onComplete) {
        this.plugin = plugin;
        this.targetLoc = targetLoc;
        this.level = level;
        this.onComplete = onComplete;
    }

    public void start() {
        Location animLoc = targetLoc.clone().add(0, -3, 0);

        for (Player p : targetLoc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(targetLoc) <= 2500) {
                plugin.getMessageManager().sendTitle(p, "titles.boss-incoming", "BOSS");
                p.sendMessage(plugin.getMessageManager().getFormattedMessage("boss-incoming"));
            }
        }
        SoundUtil.playSound(targetLoc, "ENTITY_WITHER_SPAWN", 1.0f, 0.5f);

        new BukkitRunnable() {
            int step = 0;

            @Override
            public void run() {
                if (step < 6) {
                    animLoc.add(0, 0.5, 0);
                    ParticleUtil.spawnWarningParticles(animLoc);
                } else if (step == 10) {
                    // Impact explosion particles without block damage
                    ParticleUtil.spawnExplosionParticles(targetLoc);
                    SoundUtil.playSound(targetLoc, "ENTITY_GENERIC_EXPLODE", 1.5f, 0.5f);

                    BossInstance boss = new BossInstance(plugin, level, targetLoc);
                    onComplete.accept(boss);
                    cancel();
                    return;
                }
                step++;
            }
        }.runTaskTimer(plugin, 10L, 10L);
    }
}

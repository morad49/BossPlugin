package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import com.example.bosssystem.util.SoundUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ProjectileAbility implements BossAbility {

    private final BossSystem plugin;

    public ProjectileAbility(BossSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "projectile"; }

    @Override
    public int getCooldownSeconds() { return 8; }

    @Override
    public boolean execute(BossInstance boss, Player target) {
        Location currentLoc = boss.getEntity().getEyeLocation();
        SoundUtil.playSound(currentLoc, "ENTITY_BLAZE_SHOOT", 1.0f, 0.7f);

        new BukkitRunnable() {
            int ticks = 0;
            Location projLoc = currentLoc.clone();

            @Override
            public void run() {
                if (ticks > 40 || !target.isOnline() || !boss.getEntity().isValid()) {
                    cancel();
                    return;
                }

                Vector dir = target.getEyeLocation().toVector().subtract(projLoc.toVector()).normalize().multiply(1.2);
                projLoc.add(dir);

                // Red particle trail effect
                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 1.5f);
                projLoc.getWorld().spawnParticle(Particle.DUST, projLoc, 3, 0.1, 0.1, 0.1, dust);

                if (projLoc.distanceSquared(target.getLocation()) <= 2.25) {
                    target.damage(12.0, boss.getEntity());
                    SoundUtil.playSound(projLoc, "ENTITY_GENERIC_EXPLODE", 1.0f, 1.2f);
                    cancel();
                }
                ticks++;
            }
        }.runTaskTimer(plugin, 1L, 1L);

        return true;
    }
}

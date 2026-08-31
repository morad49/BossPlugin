package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import com.example.bosssystem.util.ParticleUtil;
import com.example.bosssystem.util.SoundUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ChargeAbility implements BossAbility {

    private final BossSystem plugin;

    public ChargeAbility(BossSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "charge"; }

    @Override
    public int getCooldownSeconds() { return 14; }

    @Override
    public boolean execute(BossInstance boss, Player target) {
        Location startLoc = boss.getEntity().getLocation();
        ParticleUtil.spawnWarningParticles(startLoc);
        SoundUtil.playSound(startLoc, "ENTITY_POLAR_BEAR_WARNING", 1.2f, 0.5f);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!boss.getEntity().isValid() || !target.isOnline()) return;

            Vector chargeDir = target.getLocation().toVector().subtract(boss.getEntity().getLocation().toVector()).normalize().multiply(1.8).setY(0.2);
            
            new BukkitRunnable() {
                int runTicks = 0;
                @Override
                public void run() {
                    if (runTicks > 15 || !boss.getEntity().isValid()) {
                        cancel();
                        return;
                    }
                    boss.getEntity().setVelocity(chargeDir);
                    if (boss.getEntity().getLocation().distanceSquared(target.getLocation()) <= 4.0) {
                        target.damage(22.0, boss.getEntity());
                        target.setVelocity(chargeDir.clone().multiply(1.5).setY(0.8));
                        SoundUtil.playSound(target.getLocation(), "ENTITY_ZOMBIE_ATTACK_IRON_DOOR", 1.0f, 0.5f);
                        cancel();
                    }
                    runTicks++;
                }
            }.runTaskTimer(plugin, 1L, 1L);
        }, 20L);

        return true;
    }
}

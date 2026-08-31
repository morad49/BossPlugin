package com.bosssystem.ability;

import com.bosssystem.BossSystem;
import com.bosssystem.boss.BossInstance;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ProjectileAbility extends BossAbility {
    public ProjectileAbility(BossSystem plugin) { super(plugin, "projectile"); }

    @Override
    public void execute(BossInstance boss, LivingEntity target) {
        Location current = boss.getEntity().getEyeLocation();
        double dmg = plugin.getConfigManager().getAbilityDamage(boss.getLevel(), "projectile");
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ > 80 || !boss.isAlive()) { cancel(); return; }
                // Homing logic (very slight adjustment)
                Vector dir = target.getLocation().add(0, 1, 0).toVector().subtract(current.toVector()).normalize().multiply(0.8);
                current.add(dir);
                
                current.getWorld().spawnParticle(Particle.REDSTONE, current, 3, 0.1, 0.1, 0.1, new Particle.REDSTONEOptions(Color.RED, 1.5f));
                
                for (Entity e : current.getWorld().getNearbyEntities(current, 0.8, 0.8, 0.8)) {
                    if (e instanceof Player p) {
                        p.damage(dmg, boss.getEntity());
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}

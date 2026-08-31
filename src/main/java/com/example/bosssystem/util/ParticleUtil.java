package com.example.bosssystem.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtil {

    public static void spawnWarningParticles(Location loc) {
        if (loc == null || loc.getWorld() == null) return;
        loc.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.05);
    }

    public static void spawnExplosionParticles(Location loc) {
        if (loc == null || loc.getWorld() == null) return;
        loc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, loc, 3, 0.5, 0.5, 0.5, 0.1);
    }

    public static void spawnHealingParticles(Location loc) {
        if (loc == null || loc.getWorld() == null) return;
        Particle.DustOptions dust = new Particle.DustOptions(Color.GREEN, 1.2f);
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.clone().add(0, 1, 0), 25, 0.4, 0.6, 0.4, dust);
    }
}

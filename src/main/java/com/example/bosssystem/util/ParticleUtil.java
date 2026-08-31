package com.example.bosssystem.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtil {

    public static void spawnExplosionParticles(Location loc) {
        if (loc == null || loc.getWorld() == null) return;
        loc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, loc, 1);
    }

    public static void spawnWarningParticles(Location loc) {
        if (loc == null || loc.getWorld() == null) return;
        Particle.DustOptions orangeDust = new Particle.DustOptions(Color.ORANGE, 1.5f);
        loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0, 0.5, 0), 15, 0.3, 0.3, 0.3, orangeDust);
    }

    public static void spawnHealingParticles(Location loc) {
        if (loc == null || loc.getWorld() == null) return;
        loc.getWorld().spawnParticle(Particle.HEART, loc.clone().add(0, 1.0, 0), 10, 0.4, 0.5, 0.4);
    }

    public static void spawnExplosion(Location loc) {
        spawnExplosionParticles(loc);
    }

    public static void spawnDust(Location loc, Color color, float size) {
        if (loc == null || loc.getWorld() == null) return;
        Particle.DustOptions dust = new Particle.DustOptions(color, size);
        loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0, 1, 0), 25, 0.4, 0.6, 0.4, dust);
    }
}

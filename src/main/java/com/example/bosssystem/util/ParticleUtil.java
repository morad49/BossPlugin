package com.example.bosssystem.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtil {

    public static void spawnExplosion(Location loc) {
        if (loc == null || loc.getWorld() == null) return;
        loc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, loc, 1);
    }

    public static void spawnDust(Location loc, Color color, float size) {
        if (loc == null || loc.getWorld() == null) return;
        Particle.DustOptions dust = new Particle.DustOptions(color, size);
        loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0, 1, 0), 25, 0.4, 0.6, 0.4, dust);
    }
}

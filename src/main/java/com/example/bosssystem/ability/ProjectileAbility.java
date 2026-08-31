package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ProjectileAbility implements BossAbility {

    private final BossSystem plugin;

    public ProjectileAbility(BossSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(BossInstance boss, Player target) {
        if (boss == null || target == null) return;

        Location loc = target.getLocation();
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1.2f);

        if (loc.getWorld() != null) {
            loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0, 1, 0), 20, 0.5, 0.5, 0.5, dustOptions);
        }
    }
}

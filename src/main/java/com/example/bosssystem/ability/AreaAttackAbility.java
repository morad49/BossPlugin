package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import com.example.bosssystem.util.ParticleUtil;
import com.example.bosssystem.util.SoundUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AreaAttackAbility implements BossAbility {

    private final BossSystem plugin;

    public AreaAttackAbility(BossSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "area-attack"; }

    @Override
    public int getCooldownSeconds() { return 12; }

    @Override
    public boolean execute(BossInstance boss, Player target) {
        Location loc = boss.getEntity().getLocation();
        ParticleUtil.spawnExplosionParticles(loc);
        SoundUtil.playSound(loc, "ENTITY_DRAGON_FIREBALL_EXPLODE", 1.0f, 0.5f);

        for (Entity e : boss.getEntity().getNearbyEntities(8.0, 4.0, 8.0)) {
            if (e instanceof Player p) {
                p.damage(15.0, boss.getEntity());
                Vector push = p.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(1.5).setY(0.6);
                p.setVelocity(push);
            }
        }
        return true;
    }
}

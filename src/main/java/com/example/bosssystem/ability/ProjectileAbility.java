package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

public class ProjectileAbility implements BossAbility {
    
    private final BossSystem plugin;

    public ProjectileAbility(BossSystem plugin) {
        this.plugin = plugin;
    }

    public ProjectileAbility() {
        this.plugin = null;
    }

    @Override
    public String getName() {
        return "Projectile";
    }

    @Override
    public int getCooldownSeconds() {
        return 10;
    }

    @Override
    public boolean execute(BossInstance boss, Player target) {
        if (boss == null || target == null || boss.getEntity() == null || !boss.getEntity().isValid()) {
            return false;
        }

        boss.getEntity().launchProjectile(Fireball.class);
        return true;
    }
}

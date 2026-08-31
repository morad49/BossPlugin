package com.example.bosssystem.ability;

import com.example.bosssystem.boss.BossInstance;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

public class ProjectileAbility implements BossAbility {

    @Override
    public boolean execute(BossInstance boss, Player target) {
        // التحقق من أن البوس والهدف موجودان في اللعبة
        if (boss == null || target == null || boss.getEntity() == null || !boss.getEntity().isValid()) {
            return false;
        }

        // إطلاق مقذوف (Fireball) باتجاه اتجاه رؤية البوس
        boss.getEntity().launchProjectile(Fireball.class);
        return true;
    }
}

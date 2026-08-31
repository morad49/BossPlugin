package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import org.bukkit.entity.Player;

public class AreaAttackAbility implements BossAbility {

    private final BossSystem plugin;

    public AreaAttackAbility(BossSystem plugin) {
        this.plugin = plugin;
    }

    public AreaAttackAbility() {
        this.plugin = null;
    }

    @Override
    public String getName() {
        return "AreaAttack";
    }

    @Override
    public int getCooldownSeconds() {
        return 15;
    }

    @Override
    public boolean execute(BossInstance boss, Player target) {
        if (boss == null || boss.getEntity() == null || !boss.getEntity().isValid()) {
            return false;
        }

        return true;
    }
}

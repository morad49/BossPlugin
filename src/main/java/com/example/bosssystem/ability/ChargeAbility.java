package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import org.bukkit.entity.Player;

public class ChargeAbility implements BossAbility {

    private final BossSystem plugin;

    public ChargeAbility(BossSystem plugin) {
        this.plugin = plugin;
    }

    public ChargeAbility() {
        this.plugin = null;
    }

    @Override
    public String getName() {
        return "Charge";
    }

    @Override
    public int getCooldownSeconds() {
        return 12;
    }

    @Override
    public boolean execute(BossInstance boss, Player target) {
        if (boss == null || target == null || boss.getEntity() == null || !boss.getEntity().isValid()) {
            return false;
        }

        return true;
    }
}

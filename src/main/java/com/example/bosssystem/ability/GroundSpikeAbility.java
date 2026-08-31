package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import org.bukkit.entity.Player;

public class GroundSpikeAbility implements BossAbility {

    private final BossSystem plugin;

    public GroundSpikeAbility(BossSystem plugin) {
        this.plugin = plugin;
    }

    public GroundSpikeAbility() {
        this.plugin = null;
    }

    @Override
    public String getName() {
        return "GroundSpike";
    }

    @Override
    public int getCooldownSeconds() {
        return 20;
    }

    @Override
    public boolean execute(BossInstance boss, Player target) {
        if (boss == null || target == null || boss.getEntity() == null || !boss.getEntity().isValid()) {
            return false;
        }

        return true;
    }
}

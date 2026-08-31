package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import com.example.bosssystem.util.ParticleUtil;
import com.example.bosssystem.util.SoundUtil;
import org.bukkit.Location;
import org.bukkit.entity.EvokerFangs;

import org.bukkit.entity.Player;

public class GroundSpikeAbility implements BossAbility {

    private final BossSystem plugin;

    public GroundSpikeAbility(BossSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "ground-spike";
    }

    @Override
    public int getCooldownSeconds() {
        return 10;
    }

    @Override
    public boolean execute(BossInstance boss, Player target) {
        Location loc = target.getLocation();
        ParticleUtil.spawnWarningParticles(loc);
        SoundUtil.playSound(loc, "ENTITY_EVOKER_PREPARE_ATTACK", 1.0f, 0.5f);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (boss.getEntity().isValid() && target.isOnline()) {
                loc.getWorld().spawn(loc, EvokerFangs.class);
                SoundUtil.playSound(loc, "ENTITY_EVOKER_FANGS_ATTACK", 1.0f, 1.0f);
            }
        }, 15L);

        return true;
    }
}

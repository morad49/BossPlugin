package com.example.bosssystem.listener;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import com.example.bosssystem.util.ParticleUtil;
import com.example.bosssystem.util.SoundUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class BossDeathListener implements Listener {

    private final BossSystem plugin;

    public BossDeathListener(BossSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        BossInstance activeBoss = plugin.getBossManager().getActiveBoss();
        if (activeBoss == null) return;

        if (event.getEntity().equals(activeBoss.getEntity())) {
            event.getDrops().clear();
            event.setDroppedExp(0);

            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                plugin.getRewardManager().giveRewards(killer, activeBoss.getLevel());
            }

            ParticleUtil.spawnExplosionParticles(event.getEntity().getLocation());
            SoundUtil.playSound(event.getEntity().getLocation(), "ENTITY_WITHER_DEATH", 1.0f, 1.0f);

            plugin.getBossManager().purgeActiveBoss();
        }
    }
}

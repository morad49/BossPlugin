package com.example.bosssystem.listener;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class BossTargetListener implements Listener {

    private final BossSystem plugin;

    public BossTargetListener(BossSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBossTarget(EntityTargetEvent event) {
        BossInstance activeBoss = plugin.getBossManager().getActiveBoss();
        if (activeBoss != null && event.getEntity().equals(activeBoss.getEntity())) {
            if (!(event.getTarget() instanceof Player)) {
                event.setCancelled(true);
            }
        }
    }
}

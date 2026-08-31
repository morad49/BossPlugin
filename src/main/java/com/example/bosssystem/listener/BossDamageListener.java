package com.example.bosssystem.listener;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class BossDamageListener implements Listener {

    private final BossSystem plugin;

    public BossDamageListener(BossSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBossDamagePlayer(EntityDamageByEntityEvent event) {
        BossInstance activeBoss = plugin.getBossManager().getActiveBoss();
        if (activeBoss == null) return;

        if (event.getDamager().equals(activeBoss.getEntity()) && event.getEntity() instanceof Player target) {
            String mode = activeBoss.getDamageMode();

            switch (mode) {
                case "HEART_DAMAGE" -> { // Direct hearts damage ignoring armor
                    event.setDamage(0);
                    double damageInHp = activeBoss.getBaseDamage() * 2.0;
                    target.setHealth(Math.max(0.0, target.getHealth() - damageInHp));
                }
                case "PERCENT_HEALTH" -> {
                    event.setDamage(0);
                    double percent = activeBoss.getBaseDamage() / 100.0;
                    target.setHealth(Math.max(0.0, target.getHealth() - (target.getMaxHealth() * percent)));
                }
                default -> event.setDamage(activeBoss.getBaseDamage());
            }
        }
    }

    @EventHandler
    public void onBossEnvironmentalDamage(EntityDamageEvent event) {
        BossInstance activeBoss = plugin.getBossManager().getActiveBoss();
        if (activeBoss != null && event.getEntity().equals(activeBoss.getEntity())) {
            if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION || event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }
}

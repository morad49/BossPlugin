package com.example.bosssystem.listener;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.util.ParticleUtil;
import com.example.bosssystem.util.SoundUtil;
import org.bukkit.attribute.Attribute;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BossItemListener implements Listener {

    private final BossSystem plugin;

    public BossItemListener(BossSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRedHeartUse(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = event.getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand();

            if (plugin.getItemManager().isCustomItem(item, "red-heart")) {
                event.setCancelled(true);

                double maxHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null ? p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() : p.getMaxHealth();
                if (p.getHealth() >= maxHp) return;

                double healAmount = plugin.getConfigManager().getConfig().getDouble("items.red-heart.heal-hearts", 4.0) * 2.0;
                p.setHealth(Math.min(maxHp, p.getHealth() + healAmount));

                item.setAmount(item.getAmount() - 1);
                ParticleUtil.spawnHealingParticles(p.getLocation());
                SoundUtil.playSound(p.getLocation(), "ENTITY_PLAYER_LEVELUP", 1.0f, 1.5f);
            }
        }
    }
}

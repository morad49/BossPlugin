package com.example.bosssystem.compatibility;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class CompatibilityManager {

    public static void setEntityMaxHealth(LivingEntity entity, double maxHealth) {
        if (entity == null) return;

        Attribute healthAttribute = null;
        try {
            // Paper 1.20.6+ / Mojang Mappings
            healthAttribute = Attribute.valueOf("MAX_HEALTH");
        } catch (IllegalArgumentException e1) {
            try {
                // Legacy Spigot/Paper
                healthAttribute = Attribute.valueOf("GENERIC_MAX_HEALTH");
            } catch (IllegalArgumentException ignored) {}
        }

        if (healthAttribute != null) {
            AttributeInstance attr = entity.getAttribute(healthAttribute);
            if (attr != null) {
                attr.setBaseValue(maxHealth);
                double safeHealth = Math.min(maxHealth, attr.getValue());
                entity.setHealth(safeHealth);
            }
        }
    }
}

package com.example.bosssystem.compatibility;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class CompatibilityManager {

    @SuppressWarnings("deprecation")
    public static void setEntityMaxHealth(LivingEntity entity, double maxHealth) {
        Attribute healthAttribute = null;
        try {
            healthAttribute = Attribute.valueOf("MAX_HEALTH");
        } catch (IllegalArgumentException e) {
            try {
                healthAttribute = Attribute.valueOf("GENERIC_MAX_HEALTH");
            } catch (IllegalArgumentException ignored) {}
        }

        if (healthAttribute != null && entity.getAttribute(healthAttribute) != null) {
            entity.getAttribute(healthAttribute).setBaseValue(maxHealth);
            entity.setHealth(maxHealth);
        } else {
            entity.setMaxHealth(maxHealth);
            entity.setHealth(maxHealth);
        }
    }
}

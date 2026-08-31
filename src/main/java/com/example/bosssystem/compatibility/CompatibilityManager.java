package com.example.bosssystem.compatibility;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class CompatibilityManager {

    public static void setEntityMaxHealth(LivingEntity entity, double maxHealth) {
        if (entity == null) return;

        AttributeInstance attr = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attr == null) {
            try {
                attr = entity.getAttribute(Attribute.valueOf("GENERIC_MAX_HEALTH"));
            } catch (Exception ignored) {}
        }

        if (attr != null) {
            // ضبط قيمة الـ Attribute الأسامية
            attr.setBaseValue(maxHealth);
            // تقييد دم البوس بأقصى حد يسمح به السيرفر لمنع الكراش
            double safeHealth = Math.min(maxHealth, attr.getValue());
            entity.setHealth(safeHealth);
        }
    }
}

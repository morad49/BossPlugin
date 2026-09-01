package com.example.bosssystem.boss;

import org.bukkit.entity.EntityType;

public enum BossType {
    ZOMBIE_BOSS("§c§lZOMBIE WARLORD", EntityType.ZOMBIE, 1000.0),
    EVOKER_BOSS("§5§lDARK EVOKER", EntityType.EVOKER, 1000.0),
    RAVAGER_BOSS("§8§lDESTRUCTIVE RAVAGER", EntityType.RAVAGER, 1024.0); // البوس النهائي (الغول)

    private final String displayName;
    private final EntityType entityType;
    private final double maxHealth;

    BossType(String displayName, EntityType entityType, double maxHealth) {
        this.displayName = displayName;
        this.entityType = entityType;
        this.maxHealth = maxHealth;
    }

    public String getDisplayName() { return displayName; }
    public EntityType getEntityType() { return entityType; }
    public double getMaxHealth() { return maxHealth; }
}

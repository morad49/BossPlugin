package com.example.bosssystem.boss;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.compatibility.CompatibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class BossInstance {

    private final LivingEntity entity;
    private final BossType type;
    private final BossLevel level;
    private final BossBar bossBar;
    private final double maxHealth;
    private final double baseDamage;
    private final String damageMode;

    // Constructor 1: تستخدمه BossSpawnAnimation
    public BossInstance(BossSystem plugin, BossLevel level, Location loc) {
        this(loc, BossType.ZOMBIE_BOSS, level);
    }

    // Constructor 2: الاستدعاء المباشر عبر الموقع والنوع
    public BossInstance(Location loc, BossType type) {
        this(loc, type, null);
    }

    // Master Constructor
    public BossInstance(Location loc, BossType type, BossLevel level) {
        this.type = type != null ? type : BossType.ZOMBIE_BOSS;
        this.level = level;
        this.maxHealth = this.type.getMaxHealth();
        this.baseDamage = 20.0;
        this.damageMode = "NORMAL";

        this.entity = (LivingEntity) loc.getWorld().spawnEntity(loc, this.type.getEntityType());

        CompatibilityManager.setEntityMaxHealth(entity, maxHealth);

        String name = this.type.getDisplayName();
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);
        equipNetheriteArmor(entity);

        this.bossBar = Bukkit.createBossBar(name, BarColor.RED, BarStyle.SOLID);
        this.bossBar.setProgress(1.0);
    }

    private void equipNetheriteArmor(LivingEntity entity) {
        EntityEquipment equip = entity.getEquipment();
        if (equip != null) {
            equip.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
            equip.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
            equip.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
            equip.setBoots(new ItemStack(Material.NETHERITE_BOOTS));

            equip.setHelmetDropChance(0.0f);
            equip.setChestplateDropChance(0.0f);
            equip.setLeggingsDropChance(0.0f);
            equip.setBootsDropChance(0.0f);
        }
    }

    public void tick() {
        if (entity == null || entity.isDead() || !entity.isValid()) {
            remove();
            return;
        }
        double ratio = Math.max(0.0, Math.min(1.0, entity.getHealth() / maxHealth));
        bossBar.setProgress(ratio);

        bossBar.removeAll();
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(entity.getLocation()) <= 1600.0) {
                bossBar.addPlayer(player);
            }
        }
    }

    public void remove() {
        if (bossBar != null) {
            bossBar.removeAll();
        }
        if (entity != null && !entity.isDead()) {
            entity.remove();
        }
    }

    // Getters المطلوبة من قبل بقية الكلاسات
    public LivingEntity getEntity() { return entity; }
    public BossType getType() { return type; }
    public BossLevel getLevel() { return level; }
    public double getMaxHealth() { return maxHealth; }
    public double getBaseDamage() { return baseDamage; }
    public String getDamageMode() { return damageMode; }
    public String getName() { return type.getDisplayName(); }
    public BossBar getBossBar() { return bossBar; }
}

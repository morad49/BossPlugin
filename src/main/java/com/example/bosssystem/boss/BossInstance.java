package com.example.bosssystem.boss;

import com.example.bosssystem.compatibility.CompatibilityManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class BossInstance {

    private final LivingEntity entity;
    private final BossType type;

    public BossInstance(Location loc, BossType type) {
        this.type = type;
        this.entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type.getEntityType());

        // تعيين الصحة بدون كراش
        CompatibilityManager.setEntityMaxHealth(entity, type.getMaxHealth());

        // تعيين الاسم
        entity.setCustomName(type.getDisplayName());
        entity.setCustomNameVisible(true);

        // إلباس طقم نذررايت كامل (يمنع احتراق الزومبي بالشمس ويعطيه درع قوي)
        equipNetheriteArmor(entity);
    }

    private void equipNetheriteArmor(LivingEntity entity) {
        EntityEquipment equip = entity.getEquipment();
        if (equip != null) {
            equip.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
            equip.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
            equip.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
            equip.setBoots(new ItemStack(Material.NETHERITE_BOOTS));

            // منع تساقط الدرع عند الموت
            equip.setHelmetDropChance(0.0f);
            equip.setChestplateDropChance(0.0f);
            equip.setLeggingsDropChance(0.0f);
            equip.setBootsDropChance(0.0f);
        }
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public BossType getType() {
        return type;
    }
}

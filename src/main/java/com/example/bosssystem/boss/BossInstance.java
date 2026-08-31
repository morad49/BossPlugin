package com.example.bosssystem.boss;

import com.example.bosssystem.ability.ProjectileAbility;
import com.example.bosssystem.BossSystem;
import com.example.bosssystem.ability.*;
import com.example.bosssystem.compatibility.CompatibilityManager;
import com.example.bosssystem.config.MessageManager;
import com.example.bosssystem.region.BossRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class BossInstance {

    private final BossSystem plugin;
    private final BossLevel level;
    private final LivingEntity entity;
    private final BossBar bossBar;
    private final List<BossAbility> abilities;
    private final Map<String, Long> cooldowns;
    private final RageManager rageManager;

    private final double maxHealth;
    private final String name;
    private final String damageMode;
    private final double baseDamage;

    public BossInstance(BossSystem plugin, BossLevel level, Location spawnLoc) {
        this.plugin = plugin;
        this.level = level;
        this.abilities = new ArrayList<>();
        this.cooldowns = new HashMap<>();
        this.rageManager = new RageManager(plugin, this);

        String path = "bosses." + level.getConfigKey() + ".";
        this.name = plugin.getMessageManager().colorize(plugin.getConfigManager().getConfig().getString(path + "name", "Boss"));
        this.maxHealth = plugin.getConfigManager().getConfig().getDouble(path + "health", 500.0);
        this.damageMode = plugin.getConfigManager().getConfig().getString(path + "damage-mode", "NORMAL");
        this.baseDamage = plugin.getConfigManager().getConfig().getDouble(path + "base-damage", 10.0);

        String typeStr = plugin.getConfigManager().getConfig().getString(path + "type", "ZOMBIE");
        EntityType type = EntityType.valueOf(typeStr.toUpperCase());

        this.entity = (LivingEntity) spawnLoc.getWorld().spawnEntity(spawnLoc, type);
        this.entity.setCustomName(name);
        this.entity.setCustomNameVisible(true);
        this.entity.setRemoveWhenFarAway(false);
        CompatibilityManager.setEntityMaxHealth(this.entity, maxHealth);

        // BossBar Initialization
        String colorStr = plugin.getConfigManager().getConfig().getString(path + "bossbar.color", "RED");
        String styleStr = plugin.getConfigManager().getConfig().getString(path + "bossbar.style", "SOLID");
        this.bossBar = Bukkit.createBossBar(name, BarColor.valueOf(colorStr), BarStyle.valueOf(styleStr));

        setupAbilities();
    }

    private void setupAbilities() {
        String path = "bosses." + level.getConfigKey() + ".abilities.";
        if (plugin.getConfigManager().getConfig().getBoolean(path + "ground-spike.enabled", false)) {
            abilities.add(new GroundSpikeAbility(plugin));
        }
        if (plugin.getConfigManager().getConfig().getBoolean(path + "projectile.enabled", false)) {
            abilities.add(new ProjectileAbility(plugin));
        }
        if (plugin.getConfigManager().getConfig().getBoolean(path + "area-attack.enabled", false)) {
            abilities.add(new AreaAttackAbility(plugin));
        }
        if (plugin.getConfigManager().getConfig().getBoolean(path + "charge.enabled", false)) {
            abilities.add(new ChargeAbility(plugin));
        }
    }

    public void tick() {
        if (!entity.isValid() || entity.isDead()) return;

        updateBossBar();
        clampRegion();
        rageManager.checkRage();

        // Perform Random Available Ability
        if (!abilities.isEmpty()) {
            Player target = getValidTarget();
            if (target != null) {
                BossAbility ability = abilities.get(new Random().nextInt(abilities.size()));
                if (isCooldownReady(ability.getName())) {
                    boolean success = ability.execute(this, target);
                    if (success) {
                        int cooldown = rageManager.isRaged() ? (ability.getCooldownSeconds() / 2) : ability.getCooldownSeconds();
                        cooldowns.put(ability.getName(), System.currentTimeMillis() + (cooldown * 1000L));
                    }
                }
            }
        }
    }

    private void clampRegion() {
        BossRegion region = plugin.getRegionManager().getRegion();
        if (region != null && region.isComplete()) {
            if (!region.contains(entity.getLocation())) {
                Location clamped = region.clamp(entity.getLocation());
                entity.teleport(clamped);
            }
        }
    }

    private Player getValidTarget() {
        List<Player> nearby = new ArrayList<>();
        double searchRadius = 30.0;
        for (Player p : entity.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(entity.getLocation()) <= searchRadius * searchRadius) {
                nearby.add(p);
            }
        }
        if (nearby.isEmpty()) return null;
        return nearby.get(new Random().nextInt(nearby.size()));
    }

    private boolean isCooldownReady(String abilityName) {
        long expire = cooldowns.getOrDefault(abilityName, 0L);
        return System.currentTimeMillis() >= expire;
    }

    private void updateBossBar() {
        double progress = Math.max(0.0, Math.min(1.0, entity.getHealth() / maxHealth));
        bossBar.setProgress(progress);

        Set<Player> currentPlayers = new HashSet<>(bossBar.getPlayers());
        for (Player p : entity.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(entity.getLocation()) <= 2500.0) { // 50 blocks
                if (!currentPlayers.contains(p)) bossBar.addPlayer(p);
            } else {
                if (currentPlayers.contains(p)) bossBar.removePlayer(p);
            }
        }
    }

    public void remove() {
        bossBar.removeAll();
        if (entity.isValid()) entity.remove();
    }

    public LivingEntity getEntity() { return entity; }
    public BossLevel getLevel() { return level; }
    public double getMaxHealth() { return maxHealth; }
    public String getName() { return name; }
    public String getDamageMode() { return damageMode; }
    public double getBaseDamage() { return baseDamage; }
    public BossBar getBossBar() { return bossBar; }
}

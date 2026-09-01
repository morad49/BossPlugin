package com.example.bosssystem.task;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class BossAbilityTask extends BukkitRunnable {

    private final List<LivingEntity> activeBosses;

    public BossAbilityTask(List<LivingEntity> activeBosses) {
        this.activeBosses = activeBosses;
    }

    @Override
    public void run() {
        activeBosses.removeIf(boss -> boss == null || boss.isDead() || !boss.isValid());

        for (LivingEntity boss : activeBosses) {
            Player target = getNearestPlayer(boss, 25.0);
            if (target == null) continue;

            // 1. خاصية استهداف الطائرين بالإليترا (سهم موجه سريع)
            if (target.isGliding()) {
                shootAntiElytraArrow(boss, target);
            }

            // 2. قدرة الزومبي (ضربة قوية جداً + دفع)
            if (boss instanceof Zombie) {
                if (boss.getLocation().distance(target.getLocation()) <= 3.5 && Math.random() < 0.35) {
                    target.damage(18.0, boss);
                    Vector dir = target.getLocation().toVector().subtract(boss.getLocation().toVector()).normalize().multiply(1.8).setY(0.6);
                    target.setVelocity(dir);
                    boss.getWorld().spawnParticle(Particle.EXPLOSION, target.getLocation(), 2);
                    boss.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.5f, 0.5f);
                }
            }
            // 3. قدرة الفلجر الساحر Evoker (إخراج فكوك/تماسيح من الأرض حول اللاعب)
            else if (boss instanceof Evoker) {
                if (Math.random() < 0.25) {
                    spawnFangsAroundPlayer(target);
                }
            }
            // 4. قدرة البوس النهائي Ravager / الغول (زئير + ضربة أرضية ترفع اللاعبين)
            else if (boss instanceof Ravager) {
                if (Math.random() < 0.3) {
                    boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 2.0f, 0.7f);
                    boss.getWorld().spawnParticle(Particle.SONIC_BOOM, boss.getLocation().add(0, 1.5, 0), 1);
                    
                    for (Entity nearby : boss.getNearbyEntities(6, 6, 6)) {
                        if (nearby instanceof Player p) {
                            p.damage(22.0, boss);
                            p.setVelocity(new Vector(0, 1.3, 0)); // رفعه في الهواء
                        }
                    }
                }
            }
        }
    }

    private Player getNearestPlayer(LivingEntity boss, double radius) {
        Player nearest = null;
        double nearestDist = radius * radius;
        for (Player player : boss.getWorld().getPlayers()) {
            if (player.getGameMode().name().contains("SPECTATOR") || player.getGameMode().name().contains("CREATIVE")) continue;
            double dist = player.getLocation().distanceSquared(boss.getLocation());
            if (dist <= nearestDist) {
                nearestDist = dist;
                nearest = player;
            }
        }
        return nearest;
    }

    private void spawnFangsAroundPlayer(Player player) {
        Location loc = player.getLocation();
        for (int i = 0; i < 360; i += 45) {
            double rad = Math.toRadians(i);
            Location fangLoc = loc.clone().add(Math.cos(rad) * 2.5, 0, Math.sin(rad) * 2.5);
            player.getWorld().spawn(fangLoc, EvokerFangs.class);
        }
        player.getWorld().spawn(loc, EvokerFangs.class);
        player.getWorld().playSound(loc, Sound.ENTITY_EVOKER_FANGS_ATTACK, 1.0f, 1.0f);
    }

    private void shootAntiElytraArrow(LivingEntity boss, Player target) {
        Vector dir = target.getLocation().add(0, 0.5, 0).subtract(boss.getEyeLocation()).toVector().normalize().multiply(3.2);
        Arrow arrow = boss.launchProjectile(Arrow.class, dir);
        arrow.setDamage(15.0);
        arrow.setCritical(true);
        boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.2f, 0.4f);
    }
}

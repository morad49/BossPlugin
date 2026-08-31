package com.example.bosssystem.ability;

import com.example.bosssystem.BossSystem;
import com.example.bosssystem.boss.BossInstance;
import com.example.bosssystem.util.SoundUtil;
import org.bukkit.entity.Player;

public class RageManager {

    private final BossSystem plugin;
    private final BossInstance boss;
    private boolean raged = false;

    public RageManager(BossSystem plugin, BossInstance boss) {
        this.plugin = plugin;
        this.boss = boss;
    }

    public void checkRage() {
        if (raged) return;
        double hpPercent = boss.getEntity().getHealth() / boss.getMaxHealth();

        String path = "bosses." + boss.getLevel().getConfigKey() + ".rage.";
        double threshold = plugin.getConfigManager().getConfig().getDouble(path + "threshold", 0.30);

        if (hpPercent <= threshold) {
            this.raged = true;
            SoundUtil.playSound(boss.getEntity().getLocation(), "ENTITY_ENDER_DRAGON_GROWL", 1.0f, 0.5f);
            
            for (Player p : boss.getBossBar().getPlayers()) {
                plugin.getMessageManager().sendTitle(p, "titles.boss-rage", boss.getName());
                p.sendMessage(plugin.getMessageManager().getFormattedMessage("boss-rage").replace("%boss%", boss.getName()));
            }
        }
    }

    public boolean isRaged() {
        return raged;
    }
}

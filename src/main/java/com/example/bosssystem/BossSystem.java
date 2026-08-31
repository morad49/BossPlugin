package com.example.bosssystem;

import com.example.bosssystem.boss.BossManager;
import com.example.bosssystem.command.BossCommand;
import com.example.bosssystem.command.BossTabCompleter;
import com.example.bosssystem.config.ConfigManager;
import com.example.bosssystem.config.MessageManager;
import com.example.bosssystem.listener.BossDamageListener;
import com.example.bosssystem.listener.BossDeathListener;
import com.example.bosssystem.listener.BossItemListener;
import com.example.bosssystem.listener.BossTargetListener;
import com.example.bosssystem.region.BossRegionManager;
import com.example.bosssystem.region.BossWandListener;
import com.example.bosssystem.reward.BossItemManager;
import com.example.bosssystem.reward.BossRewardManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BossSystem extends JavaPlugin {

    private static BossSystem instance;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private BossRegionManager regionManager;
    private BossItemManager itemManager;
    private BossRewardManager rewardManager;
    private BossManager bossManager;

    @Override
    public void onEnable() {
        instance = this;

        // Load Configuration & Messages
        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.configManager.loadConfig();
        this.messageManager.loadMessages();

        // Managers initialization
        this.regionManager = new BossRegionManager(this);
        this.itemManager = new BossItemManager(this);
        this.rewardManager = new BossRewardManager(this);
        this.bossManager = new BossManager(this);

        // Register Commands
        if (getCommand("boss") != null) {
            BossCommand cmd = new BossCommand(this);
            getCommand("boss").setExecutor(cmd);
            getCommand("boss").setTabCompleter(new BossTabCompleter(this));
        }

        // Register Listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BossDamageListener(this), this);
        pm.registerEvents(new BossDeathListener(this), this);
        pm.registerEvents(new BossItemListener(this), this);
        pm.registerEvents(new BossTargetListener(this), this);
        pm.registerEvents(new BossWandListener(this), this);

        // Start Auto Spawner Task
        this.bossManager.startAutoSpawnTask();

        getLogger().info("BossSystem has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        if (bossManager != null) {
            bossManager.purgeActiveBoss();
            bossManager.stopAutoSpawnTask();
        }
        getLogger().info("BossSystem disabled safely.");
    }

    public static BossSystem getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public BossRegionManager getRegionManager() {
        return regionManager;
    }

    public BossItemManager getItemManager() {
        return itemManager;
    }

    public BossRewardManager getRewardManager() {
        return rewardManager;
    }

    public BossManager getBossManager() {
        return bossManager;
    }
}

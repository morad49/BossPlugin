package com.example.bosssystem.ability;

import com.example.bosssystem.boss.BossInstance;
import org.bukkit.entity.Player;

public interface BossAbility {
    
    String getName();
    
    int getCooldownSeconds();
    
    boolean execute(BossInstance boss, Player target);
}

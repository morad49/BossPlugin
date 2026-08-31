package com.example.bosssystem.ability;

import com.example.bosssystem.boss.BossInstance;
import org.bukkit.entity.Player;

public interface BossAbility {
    
    /**
     * تنفيذ القدرة الخاصة بالبوس
     * 
     * @param boss كائن البوس الذي ينفذ القدرة
     * @param target اللاعب المستهدف
     * @return true إذا تم تنفيذ القدرة بنجاح، و false إذا فشلت
     */
    boolean execute(BossInstance boss, Player target);
}

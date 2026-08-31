package com.example.bosssystem.util;

import org.bukkit.Location;
import org.bukkit.Sound;

public class SoundUtil {

    public static void playSound(Location loc, String soundName, float volume, float pitch) {
        if (loc == null || loc.getWorld() == null) return;
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            loc.getWorld().playSound(loc, sound, volume, pitch);
        } catch (IllegalArgumentException ignored) {}
    }
}

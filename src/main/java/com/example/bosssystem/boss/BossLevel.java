package com.example.bosssystem.boss;

public enum BossLevel {
    LEVEL_1(1, "level-1"),
    LEVEL_2(2, "level-2"),
    LEVEL_3(3, "level-3");

    private final int levelNumber;
    private final String configKey;

    BossLevel(int levelNumber, String configKey) {
        this.levelNumber = levelNumber;
        this.configKey = configKey;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getConfigKey() {
        return configKey;
    }

    public static BossLevel fromInt(int level) {
        for (BossLevel bl : values()) {
            if (bl.levelNumber == level) return bl;
        }
        return null;
    }
}

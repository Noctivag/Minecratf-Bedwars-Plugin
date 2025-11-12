package com.bedwars.achievements;

import org.bukkit.Material;

public class Achievement {

    private final String id;
    private final String name;
    private final String description;
    private final Material icon;
    private final AchievementCategory category;
    private final int targetValue;
    private final int rewardCoins;

    public Achievement(String id, String name, String description, Material icon,
                      AchievementCategory category, int targetValue, int rewardCoins) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.category = category;
        this.targetValue = targetValue;
        this.rewardCoins = rewardCoins;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getIcon() {
        return icon;
    }

    public AchievementCategory getCategory() {
        return category;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public int getRewardCoins() {
        return rewardCoins;
    }

    public enum AchievementCategory {
        GENERAL("General"),
        COMBAT("Combat"),
        TEAMWORK("Teamwork"),
        COLLECTION("Collection"),
        MASTERY("Mastery");

        private final String displayName;

        AchievementCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}

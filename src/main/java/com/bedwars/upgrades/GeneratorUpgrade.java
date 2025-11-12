package com.bedwars.upgrades;

import org.bukkit.Material;

public class GeneratorUpgrade {

    private final String name;
    private final int level;
    private final int cost;
    private final Material costType;
    private final double speedMultiplier;
    private final String description;

    public GeneratorUpgrade(String name, int level, int cost, Material costType,
                          double speedMultiplier, String description) {
        this.name = name;
        this.level = level;
        this.cost = cost;
        this.costType = costType;
        this.speedMultiplier = speedMultiplier;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getCost() {
        return cost;
    }

    public Material getCostType() {
        return costType;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return name + " " + toRoman(level);
    }

    /**
     * Convert number to Roman numerals
     */
    private static String toRoman(int number) {
        if (number < 1 || number > 4) return String.valueOf(number);
        String[] romanNumerals = {"", "I", "II", "III", "IV"};
        return romanNumerals[number];
    }
}

package com.bedwars.models;

import org.bukkit.ChatColor;

public enum TeamColor {
    RED(ChatColor.RED, "Red"),
    BLUE(ChatColor.BLUE, "Blue"),
    GREEN(ChatColor.GREEN, "Green"),
    YELLOW(ChatColor.YELLOW, "Yellow"),
    AQUA(ChatColor.AQUA, "Aqua"),
    WHITE(ChatColor.WHITE, "White"),
    PINK(ChatColor.LIGHT_PURPLE, "Pink"),
    GRAY(ChatColor.GRAY, "Gray");
    
    private final ChatColor chatColor;
    private final String displayName;
    
    TeamColor(ChatColor chatColor, String displayName) {
        this.chatColor = chatColor;
        this.displayName = displayName;
    }
    
    public ChatColor getChatColor() {
        return chatColor;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColoredName() {
        return chatColor + displayName;
    }
}

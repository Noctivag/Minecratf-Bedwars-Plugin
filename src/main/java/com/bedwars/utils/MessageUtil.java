package com.bedwars.utils;

import com.bedwars.BedwarsPlugin;
import org.bukkit.ChatColor;

import java.util.Map;

public class MessageUtil {
    
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String getMessage(String key, Map<String, String> placeholders) {
        BedwarsPlugin plugin = BedwarsPlugin.getInstance();
        String prefix = plugin.getConfig().getString("messages.prefix", "&7[&cBedwars&7] ");
        String message = plugin.getConfig().getString("messages." + key, "");
        
        // Replace placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        
        return color(prefix + message);
    }
    
    public static String getMessageWithoutPrefix(String key, Map<String, String> placeholders) {
        BedwarsPlugin plugin = BedwarsPlugin.getInstance();
        String message = plugin.getConfig().getString("messages." + key, "");
        
        // Replace placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        
        return color(message);
    }
}

package io.empyre.util;

import org.bukkit.ChatColor;

public class ChatUtil {
    public static String cl(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}

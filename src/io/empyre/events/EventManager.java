package io.empyre.events;

import org.bukkit.plugin.java.JavaPlugin;

public class EventManager {
    public EventManager(JavaPlugin pl) {
        pl.getServer().getPluginManager().registerEvents(new PlayerEvents(), pl);
    }
}

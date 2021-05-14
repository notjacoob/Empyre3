package io.empyre.events;

import io.empyre.Empyre;
import io.empyre.enums.Currencies;
import io.empyre.enums.Unicode;
import io.empyre.io.User;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerEvents implements Listener {
    public PlayerEvents() {
        r.runTaskTimer(Empyre.getPlugin(), 5L, 5L);
    }
    @EventHandler
    public void on(PlayerJoinEvent event) {
        new User(event.getPlayer());
    }
    @EventHandler
    public void on(PlayerQuitEvent event) {
        User.disconnect(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(AsyncChatEvent ev) {
        Component m = ev.message();
        ev.message(m.replaceText(b -> {
            b.match(":armor:").replacement(Unicode.ARMOR_ICON);
        }).replaceText(b -> {
            b.match(":heart:").replacement(Unicode.HEART_ICON);
        }).replaceText(b -> {
            b.match(":WideHardo:").replacement(Unicode.WideHardo);
        }));
    }

    private BukkitRunnable r = new BukkitRunnable() {
        @Override
        public void run() {
            Bukkit.getOnlinePlayers().forEach(p -> {
                Component c = Component.text(Unicode.HEART_ICON + p.getHealth() + "/" + p.getMaxHealth()).color(NamedTextColor.RED)
                        .append(Component.text("           "))
                        .append(Component.text(Unicode.ARMOR_ICON + p.getAttribute(Attribute.GENERIC_ARMOR).getValue()).color(NamedTextColor.AQUA));
                p.sendActionBar(c);
            });
        }
    };

}

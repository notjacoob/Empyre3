package io.empyre.events;

import com.google.gson.JsonObject;
import io.empyre.Empyre;
import io.empyre.enums.Currencies;
import io.empyre.enums.Unicode;
import io.empyre.io.Data;
import io.empyre.io.User;
import io.empyre.util.JsonUtil;
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
import org.intellij.lang.annotations.RegExp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PlayerEvents implements Listener {
    private final HashMap<String, String> emojis = new HashMap<>();
    public PlayerEvents() {
        r.runTaskTimer(Empyre.getPlugin(), 5L, 5L);
        try {
            JsonObject emojisJSON = JsonUtil.loadJsonFile(new File(Data.PATH + "/emojis.json"));
            emojisJSON.get("emojis").getAsJsonArray().forEach(el -> {
                JsonObject obj = el.getAsJsonObject();
                emojis.put(":" + obj.get("prefix").getAsString() + ":", obj.get("unicode").getAsString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        emojis.forEach((pr, uni) -> ev.message(m.replaceText(b -> b.match("" + pr).replacement(uni))));
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

package io.empyre.events;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.google.gson.JsonObject;
import io.empyre.Empyre;
import io.empyre.armor.Appendage;
import io.empyre.armor.ArmorSet;
import io.empyre.enums.Keys;
import io.empyre.enums.Unicode;
import io.empyre.io.Data;
import io.empyre.io.User;
import io.empyre.util.JsonUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerEvents implements Listener {
    private final HashMap<String, String> emojis = new HashMap<>();
    public PlayerEvents() {
        r.runTaskTimer(Empyre.getPlugin(), 5L, 5L);
        try {
            JsonObject emojisJSON = JsonUtil.loadJsonFile(new File(Data.PATH + "/emojis.json"));
            System.out.println(emojisJSON == null);
            emojisJSON.get("emojis").getAsJsonArray().forEach(el -> {
                JsonObject obj = el.getAsJsonObject();
                emojis.put(obj.get("prefix").getAsString(), obj.get("unicode").getAsString());
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
        AtomicReference<Component> m = new AtomicReference<>(ev.message());
        emojis.forEach((pr, uni) -> {
            ev.message(m.get().replaceText(b -> b.match(":" + pr + ":").replacement(uni)));
            m.set(ev.message());
        });
    }
    @EventHandler
    public void on(PlayerArmorChangeEvent ev) {
        if (ev.getOldItem() != null) {
            if (ev.getOldItem().hasItemMeta()) {
                if (ev.getOldItem().getItemMeta().getPersistentDataContainer().has(Keys.ARMOR_SET, PersistentDataType.STRING)) {
                    String value = ev.getOldItem().getItemMeta().getPersistentDataContainer().get(Keys.ARMOR_SET, PersistentDataType.STRING);
                    ArmorSet set = ArmorSet.getSets().get(value);
                    if (set != null) {
                        if (ev.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH) != null && set.get(Appendage.fromSlotType(ev.getSlotType())).getHealthIncrease() > 0) {
                            double maxHealth = ev.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                            double newHealth = set.get(Appendage.fromSlotType(ev.getSlotType())).getHealthIncrease();
                            ev.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth - newHealth);
                            if (ev.getPlayer().getHealth() > maxHealth - newHealth) {
                                ev.getPlayer().damage(ev.getPlayer().getHealth() - maxHealth - newHealth);
                            }
                        }
                    }
                }
        }
        }
        if (ev.getNewItem() != null) {
            if (ev.getNewItem().hasItemMeta()) {
                if (ev.getNewItem().getItemMeta().getPersistentDataContainer().has(Keys.ARMOR_SET, PersistentDataType.STRING)) {
                    String value = ev.getNewItem().getItemMeta().getPersistentDataContainer().get(Keys.ARMOR_SET, PersistentDataType.STRING);
                    ArmorSet set = ArmorSet.getSets().get(value);
                    if (set != null) {
                        if (ev.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH) != null && set.get(Appendage.fromSlotType(ev.getSlotType())).getHealthIncrease() > 0) {
                            double maxHealth = ev.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                            double newHealth = set.get(Appendage.fromSlotType(ev.getSlotType())).getHealthIncrease();
                            ev.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth + newHealth);
                        }
                    }
                }
            }
        }
    }

    private final DecimalFormat f = new DecimalFormat("#.#");
    private BukkitRunnable r = new BukkitRunnable() {
        @Override
        public void run() {
            Bukkit.getOnlinePlayers().forEach(p -> {
                Component c = Component.text(Unicode.HEART_ICON + " " + f.format(p.getHealth()) + "/" + p.getMaxHealth()).color(NamedTextColor.RED)
                        .append(Component.text("           "))
                        .append(Component.text(Unicode.ARMOR_ICON +" "+ p.getAttribute(Attribute.GENERIC_ARMOR).getValue()).color(NamedTextColor.AQUA));
                p.sendActionBar(c);
            });
        }
    };

}

package io.empyre.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.empyre.Empyre;
import io.empyre.armor.ArmorSet;
import io.empyre.enums.Unicode;
import org.bukkit.entity.Player;

public class ArmorInventory implements InventoryProvider {
    private final ArmorSet set;
    public static SmartInventory create(ArmorSet set) {
        return SmartInventory.builder()
                .id("armorInv-" + set.getKey())
                .manager(Empyre.getPlugin().getInventoryManager())
                .provider(new ArmorInventory(set))
                .size(3, 9)
                .title("Armor set - " + set.getKey())
                .closeable(true)
                .build();
    }
    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 1, ClickableItem.of(set.getHelmet().toStack(), e->e.setCancelled(false)));
        contents.set(1, 3, ClickableItem.of(set.getChestplate().toStack(), e->e.setCancelled(false)));
        contents.set(1, 5, ClickableItem.of(set.getLeggings().toStack(), e->e.setCancelled(false)));
        contents.set(1, 7, ClickableItem.of(set.getBoots().toStack(), e->e.setCancelled(false)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        init(player, contents);
    }

    private ArmorInventory(ArmorSet set) {
        this.set=set;
    }
}

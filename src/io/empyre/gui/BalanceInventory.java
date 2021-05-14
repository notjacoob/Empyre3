package io.empyre.gui;

import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.empyre.Empyre;
import io.empyre.enums.Currencies;
import io.empyre.enums.Unicode;
import io.empyre.io.User;
import io.empyre.util.ChatUtil;
import io.empyre.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BalanceInventory implements InventoryProvider {

    public static SmartInventory create(Player p) {
        return SmartInventory.builder()
                .id("balInv-" + p.getName())
                .manager(Empyre.getPlugin().getInventoryManager())
                .provider(new BalanceInventory())
                .size(3, 9)
                .title(Unicode.BAL_INV)
                .closeable(true)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        User user = User.getUser(player);
        ItemStack one = new ItemBuilder(Material.DEAD_BUSH).name(ChatUtil.cl("&b" + Currencies.CURRENCY_ONE.name() + ": " + user.getMoney(Currencies.CURRENCY_ONE))).make();
        ItemStack two = new ItemBuilder(Material.DEAD_BUSH).name(ChatUtil.cl("&6" + Currencies.CURRENCY_TWO.name() + ": " + user.getMoney(Currencies.CURRENCY_TWO))).make();
        ItemStack three = new ItemBuilder(Material.DEAD_BUSH).name(ChatUtil.cl("&c" + Currencies.CURRENCY_THREE.name() + ": " + user.getMoney(Currencies.CURRENCY_THREE))).make();
        contents.set(1, 2, ClickableItem.of(setModelData(one, 1), e -> e.setCancelled(true)));
        contents.set(1, 4, ClickableItem.of(setModelData(two, 2), e -> e.setCancelled(true)));
        contents.set(1, 6, ClickableItem.of(setModelData(three, 3), e -> e.setCancelled(true)));
    }
    private ItemStack setModelData(ItemStack stack, int data) {
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(data);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        init(player, contents);
    }
}

package io.empyre.commands;

import io.empyre.Empyre;
import io.empyre.gui.BalanceInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdBal implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be in game to do this");
            return true;
        }
        Player p = (Player) sender;
        if (sender.hasPermission("empyre.balance")) {
            BalanceInventory.create(p).open(p);
        } else {
            Empyre.getPlugin().getAudiences().sender(sender).sendMessage(Component.text("You don't have permission to do that!").color(NamedTextColor.DARK_RED));
        }
        return true;
    }
}

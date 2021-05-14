package io.empyre.commands;

import io.empyre.armor.ArmorSet;
import io.empyre.gui.ArmorInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdArmor implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("empyre.armor")) {
                if (args.length > 0) {
                    String set = args[0];
                    if (ArmorSet.getSets().containsKey(set)) {
                        ArmorSet sset = ArmorSet.getSets().get(set);
                        ArmorInventory.create(sset).open(p);
                    }
                }
            }
        }
        return true;
    }
}

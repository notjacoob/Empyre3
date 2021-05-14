package io.empyre.commands;

import io.empyre.util.ChatUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CmdDamage implements TabExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("empyre.damage")) {
            if (args.length >= 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && NumberUtils.isNumber(args[1])) {
                    double damage = Double.parseDouble(args[1]);
                    target.damage(damage);
                    target.sendMessage(ChatUtil.cl("&cYou have been smitten!"));
                    sender.sendMessage(ChatUtil.cl("&8You damaged &b" + target.getName() + " &8for &b" + damage + " &8damage!"));
                } else {
                    sender.sendMessage(ChatUtil.cl("&cInvalid args!"));
                }
            } else {
                sender.sendMessage(ChatUtil.cl("&cInvalid args!"));
            }
        } else {
            sender.sendMessage(ChatUtil.cl("&cYou don't have permission to do that!"));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        } else {
            return Collections.singletonList("(damage amount)");
        }
    }
}

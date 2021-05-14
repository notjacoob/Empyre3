package io.empyre.commands;

import io.empyre.Empyre;
import io.empyre.enums.Currencies;
import io.empyre.io.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CmdEco implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("empyre.eco")) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("subtract")) {
                if (sender.hasPermission("empyre.eco.admin")) {
                    if (args.length >= 2 && Currencies.valueOfOrNull(args[1]) != null) {
                        final Currencies c = Currencies.valueOf(args[1]);
                        if (args.length >= 3 && Bukkit.getPlayer(args[2]) != null) {
                            Player target = Bukkit.getPlayer(args[2]);
                            assert target != null;
                            User u = User.getUser(target);
                            if (args.length >= 4 && NumberUtils.isNumber(args[3])) {
                                int number = Integer.parseInt(args[3]);
                                int old = u.getMoney(c);
                                if (args[0].equalsIgnoreCase("add")) u.addMoney(c, number); else u.subtractMoney(c, number);
                                Empyre.getPlugin().getAudiences().sender(sender).sendMessage(
                                        Component.text("Operation complete!\n").color(NamedTextColor.DARK_AQUA)
                                                .append(Component.text("Old balance: ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text(old).color(NamedTextColor.AQUA))
                                                .append(Component.text("\nNew balance: ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text(u.getMoney(c)).color(NamedTextColor.AQUA))
                                );
                            } else {
                                Empyre.getPlugin().getAudiences().sender(sender).sendMessage(Component.text("Invalid money amount!").color(NamedTextColor.RED));
                            }
                        } else {
                            Empyre.getPlugin().getAudiences().sender(sender).sendMessage(Component.text("Invalid player!").color(NamedTextColor.RED));
                        }
                    } else {
                        Empyre.getPlugin().getAudiences().sender(sender).sendMessage(Component.text("Invalid currency!").color(NamedTextColor.RED));
                    }
                } else {
                    noPerm(sender);
                }
            } else if (args[0].equalsIgnoreCase("balance")) {

                if (sender.hasPermission("empyre.eco.balance")) {
                    if (args.length >= 2) {
                        boolean all = args[1].equalsIgnoreCase("all");
                        Currencies c = Currencies.valueOfOrNull(args[1]);
                        if (args.length >= 3 && Bukkit.getPlayer(args[2]) != null) {
                            User target = User.getUser(Bukkit.getPlayer(args[2]));
                            if (!all) {
                                Empyre.getPlugin().getAudiences().sender(sender).sendMessage(
                                        Component.text(c.name() + " balance for " + target.getPlayer().getName() + ": ").color(NamedTextColor.GRAY).append(Component.text(target.getMoney(c)).color(NamedTextColor.AQUA))
                                );
                            } else {
                                Empyre.getPlugin().getAudiences().sender(sender).sendMessage(
                                        Component.text("Balances for " + target.getPlayer().getName()).decorate(TextDecoration.BOLD).color(NamedTextColor.DARK_AQUA)
                                        .append(Component.text("\nCurrency one: ").color(NamedTextColor.GRAY)).append(Component.text(target.getMoney(Currencies.CURRENCY_ONE)).color(NamedTextColor.AQUA))
                                        .append(Component.text("\nCurrency two: ").color(NamedTextColor.GRAY)).append(Component.text(target.getMoney(Currencies.CURRENCY_TWO)).color(NamedTextColor.AQUA))
                                        .append(Component.text("\nCurrency three: ").color(NamedTextColor.GRAY)).append(Component.text(target.getMoney(Currencies.CURRENCY_THREE)).color(NamedTextColor.AQUA))
                                );
                            }
                        } else {
                            Empyre.getPlugin().getAudiences().sender(sender).sendMessage(Component.text("Invalid player!").color(NamedTextColor.RED));
                        }
                    } else {
                        Empyre.getPlugin().getAudiences().sender(sender).sendMessage(Component.text("Invalid currency!").color(NamedTextColor.RED));
                    }
                } else {
                    noPerm(sender);
                }
            } else if (args[0].equalsIgnoreCase("reset")) {
                if (sender.hasPermission("empyre.eco.reset")) {
                    if (args.length>=2) {
                        boolean all = args[1].equalsIgnoreCase("all");
                        Currencies c = Currencies.valueOfOrNull(args[1]);
                        if (args.length>=3&&Bukkit.getPlayer(args[2])!=null){
                            User target = User.getUser(Bukkit.getPlayer(args[2]));
                            if (!all) {
                                target.subtractMoney(c, target.getMoney(c));
                                Empyre.getPlugin().getAudiences().sender(sender).sendMessage(
                                        Component.text("Reset player ").color(NamedTextColor.GRAY).append(Component.text(target.getPlayer().getName() + "'s").color(NamedTextColor.AQUA)).append(Component.text(" " + c.name() + " to 0").color(NamedTextColor.DARK_GRAY))
                                );
                                Empyre.getPlugin().getAudiences().player(target.getPlayer()).sendMessage(
                                        Component.text("Your " + c.name() + " balance was reset!").color(NamedTextColor.RED)
                                );
                            } else {
                                target.subtractMoney(Currencies.CURRENCY_ONE, target.getMoney(Currencies.CURRENCY_ONE));
                                target.subtractMoney(Currencies.CURRENCY_TWO, target.getMoney(Currencies.CURRENCY_TWO));
                                target.subtractMoney(Currencies.CURRENCY_THREE, target.getMoney(Currencies.CURRENCY_THREE));
                                Empyre.getPlugin().getAudiences().sender(sender).sendMessage(
                                        Component.text("Reset player ").color(NamedTextColor.GRAY).append(Component.text(target.getPlayer().getName() + "'s").color(NamedTextColor.AQUA)).append(Component.text(" balances to 0").color(NamedTextColor.DARK_GRAY))
                                );
                                Empyre.getPlugin().getAudiences().player(target.getPlayer()).sendMessage(
                                        Component.text("Your " + Currencies.CURRENCY_ONE.name() + ", " + Currencies.CURRENCY_TWO.name() + ", and "+ Currencies.CURRENCY_THREE.name() + " balances were reset!").color(NamedTextColor.RED)
                                );
                            }
                        }
                    }
                }
            }
        } else {
            noPerm(sender);
        }
        return true;
    }
    private void noPerm(CommandSender pl) {
        Empyre.getPlugin().getAudiences().sender(pl).sendMessage(Component.text("You don't have permission to do that!").color(NamedTextColor.DARK_RED));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("add", "subtract", "balance", "reset");
        } else if (args.length == 2) {
            List<String> l = new ArrayList<>(List.of("CURRENCY_ONE", "CURRENCY_TWO", "CURRENCY_THREE"));
            if (args[0].equalsIgnoreCase("reset")) l.add("all");
            return l;
        } else if (args.length == 3) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }else if (args.length == 4 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("subtract"))) {
            return List.of("(currency amount)");
        } else {
            return Collections.emptyList();
        }
    }
}

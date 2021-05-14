package io.empyre;

import fr.minuskube.inv.InventoryManager;
import io.empyre.armor.ArmorSet;
import io.empyre.commands.CmdArmor;
import io.empyre.commands.CmdBal;
import io.empyre.commands.CmdDamage;
import io.empyre.commands.CmdEco;
import io.empyre.events.EventManager;
import io.empyre.io.Data;
import io.empyre.io.User;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Empyre extends JavaPlugin {

    private static Empyre plugin;
    private BukkitAudiences audiences;
    private final InventoryManager manager = new InventoryManager(this);

    public void onEnable() {
        plugin = this;
        Data.start();
        audiences = BukkitAudiences.create(this);
        new EventManager(this);
        registerCommands();
        manager.init();
        loadArmor();
        if (Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(User::new);
        }
    }
    private void loadArmor() {
        File f = new File(Data.PATH + "/armor/");
        if (f.listFiles() == null) return;
        for (File file : f.listFiles()) {
            if (file.getPath().endsWith(".json")) {
                try {
                    ArmorSet.load(file.getName().replace(".json", ""));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void onDisable() {
        User.getOnlineUsers().forEach(u -> {
            try {
                u.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void registerCommands() {
        this.getCommand("eco").setExecutor(new CmdEco());
        this.getCommand("bal").setExecutor(new CmdBal());
        this.getCommand("damage").setExecutor(new CmdDamage());
        this.getCommand("armor").setExecutor(new CmdArmor());
    }
    public BukkitAudiences getAudiences() {return audiences;}
    public static Empyre getPlugin() {
        return plugin;
    }
    public InventoryManager getInventoryManager() { return manager;}
}

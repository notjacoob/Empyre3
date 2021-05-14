package io.empyre.io;

import io.empyre.api.BankAccount;
import io.empyre.enums.Currencies;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class User extends Data implements BankAccount {

    private static final Map<UUID, User> users = new HashMap<>();
    public static Collection<User> getOnlineUsers() {
        return users.values();
    }
    public static User getUser(Player p) {
        return users.get(p.getUniqueId());
    }
    public static void disconnect(Player p) {
        try {
            users.get(p.getUniqueId()).save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        users.remove(p.getUniqueId());
    }

    private int ecoValueOne = 0;
    private int ecoValueTwo = 0;
    private int ecoValueThree = 0;
    private final Player player;
    //TODO make names for the economy values
    public User(Player player) {
        super(Data.PATH + "/users/" + player.getUniqueId() + ".yml");
        if (!file.exists()) {
            try {
                super.create();
            } catch (IOException e) {
                System.out.println("Could not create data for " + player.getName() + "(" + player.getUniqueId() + ")");
                player.kickPlayer("System error!");
            }
        } else {
            System.out.println(config.getInt("eco.one"));
            ecoValueOne = config.getInt("eco.one");
            ecoValueTwo = config.getInt("eco.two");
            ecoValueThree = config.getInt("eco.three");
        }
        this.player=player;
        users.put(player.getUniqueId(), this);
    }
    public Player getPlayer() {
        return player;
    }



    private void setIfNotSet(YamlConfiguration config, String path, Object obj) {
        if (!config.isSet(path)) {
            config.set(path, obj);
        }
    }

    @Override
    public void save() throws IOException {
        config.set("eco.one", ecoValueOne);
        config.set("eco.two", ecoValueTwo);
        config.set("eco.three", ecoValueThree);
        config.save(file);
    }

    @Override
    protected boolean writeInitial() {
        if (config != null) {
            setIfNotSet(config, "eco.one", 0);
            setIfNotSet(config, "eco.two", 0);
            setIfNotSet(config, "eco.three", 0);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getMoney(Currencies currency) {
        if (currency == Currencies.CURRENCY_ONE) return ecoValueOne; else if (currency == Currencies.CURRENCY_TWO) return ecoValueTwo; else return ecoValueThree;
    }

    @Override
    public void addMoney(Currencies currency, int money) {
        if (currency == Currencies.CURRENCY_ONE) {
            ecoValueOne+=money;
        } else if (currency == Currencies.CURRENCY_TWO) {
            ecoValueTwo+=money;
        } else {
            ecoValueThree+=money;
        }
    }

    @Override
    public void subtractMoney(Currencies currency, int money) {
        if (currency == Currencies.CURRENCY_ONE) {
            ecoValueOne-=money;
        } else if (currency == Currencies.CURRENCY_TWO) {
            ecoValueTwo-=money;
        } else {
            ecoValueThree-=money;
        }
    }
}

package io.empyre.armor;

import org.bukkit.Material;

public enum Appendage {
    HELMET("generic.armorHead"),
    CHESTPLATE("generic.armorBody"),
    LEGGINGS("generic.armorLegs"),
    BOOTS("generic.armorFeet");
    public final String generic;
    Appendage(String generic) {
        this.generic=generic;
    }
    public Material withType(ArmorType type) {
        switch (this) {
            case HELMET:
                switch (type) {
                    case DIAMOND:
                        return Material.DIAMOND_HELMET;
                    case GOLD:
                        return Material.GOLDEN_HELMET;
                    case IRON:
                        return Material.IRON_HELMET;
                    case CHAIN:
                        return Material.CHAINMAIL_HELMET;
                    case LEATHER:
                        return Material.LEATHER_HELMET;
                }
            case CHESTPLATE:
                switch (type) {
                    case DIAMOND:
                        return Material.DIAMOND_CHESTPLATE;
                    case GOLD:
                        return Material.GOLDEN_CHESTPLATE;
                    case IRON:
                        return Material.IRON_CHESTPLATE;
                    case CHAIN:
                        return Material.CHAINMAIL_CHESTPLATE;
                    case LEATHER:
                        return Material.LEATHER_CHESTPLATE;
                }
            case BOOTS:
                switch (type) {
                    case DIAMOND:
                        return Material.DIAMOND_BOOTS;
                    case GOLD:
                        return Material.GOLDEN_BOOTS;
                    case IRON:
                        return Material.IRON_BOOTS;
                    case CHAIN:
                        return Material.CHAINMAIL_BOOTS;
                    case LEATHER:
                        return Material.LEATHER_BOOTS;
                }
            case LEGGINGS:
                switch (type) {
                    case DIAMOND:
                        return Material.DIAMOND_LEGGINGS;
                    case GOLD:
                        return Material.GOLDEN_LEGGINGS;
                    case IRON:
                        return Material.IRON_LEGGINGS;
                    case CHAIN:
                        return Material.CHAINMAIL_LEGGINGS;
                    case LEATHER:
                        return Material.LEATHER_LEGGINGS;
                }
            default: return Material.DEAD_BUSH; // never reached
        }
    }
}

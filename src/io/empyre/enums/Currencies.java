package io.empyre.enums;

public enum Currencies {
    CURRENCY_ONE,
    CURRENCY_TWO,
    CURRENCY_THREE;
    public static Currencies valueOfOrNull(String v) {
        try {
            return valueOf(v);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}

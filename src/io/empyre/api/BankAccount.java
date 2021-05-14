package io.empyre.api;

import io.empyre.enums.Currencies;

public interface BankAccount {
    int getMoney(Currencies currency);
    void addMoney(Currencies currency, int money);
    void subtractMoney(Currencies currency, int money);
}

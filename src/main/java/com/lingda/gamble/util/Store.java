package com.lingda.gamble.util;

public class Store {
    private static String accountName;

    public static String getAccountName() {
        return accountName;
    }

    public static void setAccountName(String accountName) {
        Store.accountName = accountName;
    }
}

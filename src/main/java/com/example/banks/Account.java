package com.example.banks;

public class Account {
    private static String currentUsername = "";
    private String username;
    private String passwordHash;
    private double balance;

    public Account(String username, String passwordHash, double balance) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.balance = balance;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

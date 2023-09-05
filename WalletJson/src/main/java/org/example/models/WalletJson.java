package org.example.models;

public class WalletJson {
    private String action;
    private int id;
    private double amount;

    public WalletJson(String action, int id, double amount) {
        this.action = action;
        this.id = id;
        this.amount = amount;
    }

    public WalletJson() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}


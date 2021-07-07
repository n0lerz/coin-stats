package com.bitvavo.api.example.models;

public class Asset {
    private String symbol;
    private double amount;

    public Asset() {

    }

    public Asset(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMarket()
    {
        String market = symbol + "-EUR";
        return market;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

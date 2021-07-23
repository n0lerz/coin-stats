package com.bitvavo.api.example.models;

public class Asset {
    private String symbol;
    private double currentTickerPrice;
    private double amount;
    private double amountBought;
    private double totalValue;
    private double totalCost;
    private double valueSold;
    private double profit;
    private double stakingRewardAmount;
    private double stakingRewardValue;
    private double averagePrice;

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

    public String getMarket() {
        String market = symbol + "-EUR";
        return market;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountBought() {
        return amountBought;
    }

    public void setAmountBought(double amountBought) {
        this.amountBought = amountBought;
    }

    public double getStakingRewardAmount() {
        return stakingRewardAmount;
    }

    public void setStakingRewardAmount(double stakingRewardAmount) {
        this.stakingRewardAmount = stakingRewardAmount;
    }

    public double getStakingRewardValue() {
        return stakingRewardValue;
    }

    public void setStakingRewardValue(double stakingRewardValue) {
        this.stakingRewardValue = stakingRewardValue;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getCurrentTickerPrice() {
        return currentTickerPrice;
    }

    public void setCurrentTickerPrice(double currentTickerPrice) {
        this.currentTickerPrice = currentTickerPrice;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getValueSold() {
        return valueSold;
    }

    public void setValueSold(double valueSold) {
        this.valueSold = valueSold;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }
}

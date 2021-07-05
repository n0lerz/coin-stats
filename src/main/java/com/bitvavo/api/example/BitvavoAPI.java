package com.bitvavo.api.example;

import com.bitvavo.api.*;
import com.bitvavo.api.example.models.Asset;
import org.json.*;

import java.util.*;

/*
 * This is an BitvavoAPI utilising all functions of the node Bitvavo API wrapper.
 * The APIKEY and APISECRET should be replaced by your own key and secret.
 * For public functions the APIKEY and SECRET can be removed.
 * Documentation: https://docs.bitvavo.com
 * Bitvavo: https://bitvavo.com
 * README: https://github.com/bitvavo/java-bitvavo-api
 */

public class BitvavoAPI {
    Bitvavo bitvavo;

    //for testing only
    public BitvavoAPI() {
        bitvavo = new Bitvavo(new JSONObject("{" +
                "APIKEY: '" + APIKeysLocal.KEY + "', " +
                "APISECRET: '" + APIKeysLocal.SECRET + "', " +
                "RESTURL: 'https://api.bitvavo.com/v2'," +
                "WSURL: 'wss://ws.bitvavo.com/v2/'," +
                "ACCESSWINDOW: 10000, " +
                "DEBUGGING: false }"));
    }

    //for sign in screen
    public BitvavoAPI(String APIKey, String APISecret) {
        bitvavo = new Bitvavo(new JSONObject("{" +
                "APIKEY: '" + APIKey + "', " +
                "APISECRET: '" + APISecret + "', " +
                "RESTURL: 'https://api.bitvavo.com/v2'," +
                "WSURL: 'wss://ws.bitvavo.com/v2/'," +
                "ACCESSWINDOW: 10000, " +
                "DEBUGGING: false }"));
    }

    public List<Asset> getAllAssets() {
        JSONArray response;
        List<Asset> assetList = new ArrayList<Asset>();
        response = bitvavo.assets(new JSONObject());
        for (int i = 0; i < response.length(); i++) {
//            System.out.println(response.getJSONObject(i).toString(2));
            Asset asset = new Asset(response.getJSONObject(i).getString("symbol"));
            assetList.add(asset);
        }

        for (Asset asset : assetList) //for testing
        {
            System.out.println(asset.getSymbol());
        }


        return assetList;
    }

    public double getTotalAmountBought(Asset asset) {
        JSONArray response;
        response = bitvavo.trades("BTC-EUR", new JSONObject());
        for (int i = 0; i < response.length(); i++) {
            boolean settled = response.getJSONObject(i).getBoolean("settled");
            String side = response.getJSONObject(i).getString("side");
            double amount = response.getJSONObject(i).getDouble("amount");
            double totalAmountBought = 0.0;
            if (settled == true) {
                if (side.equals("buy")) {
                    totalAmountBought += amount;
                } else if (side.equals("sell")) {
                    totalAmountBought -= amount;
                }
            }
        }
        return
    }


    public double getTotalAmount(Asset asset) { // TODO fix this
        JSONArray response;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("symbol", asset.getSymbol());
        response = bitvavo.balance(jsonObject);
        double available = response.getJSONObject(0).getDouble("available");
        double inOrder = response.getJSONObject(0).getDouble("inOrder");
        double totalAmount = available + inOrder;
        return totalAmount;
    }

    public double calculateStakingRewardAmount(Asset asset) {
        double stakingReward = 0.0;
        stakingReward = getTotalAmount(asset) - getTotalAmountBought(asset);
        System.out.println("Staking reward: " + stakingReward);
        return stakingReward;
    }

    public double calculateStakingRewardValue(Asset asset) {
        double stakingRewardValue = 0.0;
        stakingRewardValue = calculateStakingRewardAmount(asset) * getTickerPrice(asset);
        System.out.println("stakingRewardValue: " + stakingRewardValue);
        return stakingRewardValue;
    }

    public double getTickerPrice(Asset asset) {
        JSONArray response;
        double tickerPrice = 0.0;
        JSONObject json = new JSONObject();
        json.put("market", asset.getMarket());
        response = bitvavo.tickerPrice(json);
        tickerPrice = response.getJSONObject(0).getDouble("price"); //TEST THIS
        System.out.println("Ticker price: " + tickerPrice);
        return tickerPrice;
    }

    public void printAssetStats(Asset asset) { //TODO split into smaller methods
        JSONArray response;

        double currentAssetPrice = 0.0;
        JSONObject json = new JSONObject();
        json.put("market", asset.getMarket());
        response = bitvavo.tickerPrice(json);
        for (int i = 0; i < response.length(); i++) {
//            System.out.println(response.getJSONObject(i).toString(2));
            currentAssetPrice = response.getJSONObject(i).getDouble("price");
        }


        double averagePrice, totalCost, totalProfit, filledAmount, totalAmount, fee,
                filledAmountQuote, totalStakingRewards, totalStakingRewardsValue, totalValue,
                totalValueSold;
        totalValue = totalCost = totalProfit = totalAmount = totalValueSold = 0.0;
        String side, status;
//        side = status = "";
        response = bitvavo.getOrders(asset.getMarket(), new JSONObject());
        for (int i = 0; i < response.length(); i++) {
//            System.out.println(response.getJSONObject(i).toString(2));
            side = response.getJSONObject(i).getString("side");
            filledAmount = response.getJSONObject(i).getDouble("filledAmount");
            filledAmountQuote = response.getJSONObject(i).getDouble("filledAmountQuote");
            fee = response.getJSONObject(i).getDouble("feePaid");
            status = response.getJSONObject(i).getString("status");
            if (status.equals("filled")) {
                if (side.equals("buy")) {
                    totalCost = totalCost + filledAmountQuote + fee;
                    totalAmount += filledAmount;
                } else if (side.equals("sell")) {
                    totalCost += fee;
                    totalValueSold += filledAmountQuote;
                    totalAmount -= filledAmount;
                }
            }
        }
        //TODO check if asset has staking rewards
        averagePrice = (totalCost - totalValueSold) / totalAmount;
        totalValue = totalAmount * currentAssetPrice;
        totalProfit = (totalValue + totalValueSold) - totalCost; // (average price * amount)-(current price * amount)?
        System.out.println("Total value: " + totalValue);
        System.out.println("Total value sold: " + totalValueSold);
        System.out.println("Total amount: " + totalAmount); // bitvavo.balance
        System.out.println("Total cost: " + totalCost);
        System.out.println("Total profit: " + totalProfit);
        System.out.println("Average price: " + averagePrice);
        System.out.println("Current price: " + currentAssetPrice);

        // response = bitvavo.trades("BTC-EUR", new JSONObject());
        // for(int i = 0; i < response.length(); i ++) {
        //   System.out.println(response.getJSONObject(i).toString(2));
        // }
        //TODO replace getorders with trades

        int remaining = bitvavo.getRemainingLimit();
        System.out.println("remaining limit is " + remaining);
    }
}

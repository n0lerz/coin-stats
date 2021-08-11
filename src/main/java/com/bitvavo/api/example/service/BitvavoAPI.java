package com.bitvavo.api.example.service;

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
            Asset asset = new Asset(response.getJSONObject(i).getString("symbol"));
            assetList.add(asset);
        }
        return assetList;
    }

    public List<Asset> getAllOwnedAssets() {
        JSONArray response;
        List<Asset> ownedAssetList = new ArrayList<Asset>();
        response = bitvavo.balance(new JSONObject());
        for (int i = 0; i < response.length(); i++) {
            Asset ownedAsset = new Asset(response.getJSONObject(i).getString("symbol"));
            ownedAssetList.add(ownedAsset);
        }
        return ownedAssetList;
    }

/*
    public double getTotalAmountBought(Asset asset) {
        JSONArray response;
        double totalAmountBought = 0.0;
        response = bitvavo.trades(asset.getMarket(), new JSONObject());
        for (int i = 0; i < response.length(); i++) {
            boolean settled = response.getJSONObject(0).getBoolean("settled");
            String side = response.getJSONObject(0).getString("side");
            double amount = response.getJSONObject(0).getDouble("amount");
            if (settled == true) {
                if (side.equals("buy")) {
                    totalAmountBought += amount;
                }
            }
        }
        return totalAmountBought;
    }
*/

    public double getTotalAmountBought(Asset asset) {
        JSONArray response;
        double totalAmountBought = 0.0;
        double filledAmount;
        String side, status;
        response = bitvavo.getOrders(asset.getMarket(), new JSONObject());
        for (int i = 0; i < response.length(); i++) {
            side = response.getJSONObject(i).getString("side");
            filledAmount = response.getJSONObject(i).getDouble("filledAmount");
            status = response.getJSONObject(i).getString("status");
            if (status.equals("filled")) {
                if (side.equals("buy")) {
                    totalAmountBought += filledAmount;
                }
            }
        }
            return totalAmountBought;
    }
    public double getTotalAmountSold(Asset asset) {
        JSONArray response;
        double totalAmountSold = 0.0;
        double filledAmount;
        String side, status;
        response = bitvavo.getOrders(asset.getMarket(), new JSONObject());
        for (int i = 0; i < response.length(); i++) {
            side = response.getJSONObject(i).getString("side");
            filledAmount = response.getJSONObject(i).getDouble("filledAmount");
            status = response.getJSONObject(i).getString("status");
            if (status.equals("filled")) {
                if (side.equals("sell")) {
                    totalAmountSold += filledAmount;
                }
            }
        }
        return totalAmountSold;
    }

    public double getTotalAmount(Asset asset) {
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
        stakingReward = getTotalAmount(asset) + getTotalAmountSold(asset) - getTotalAmountBought(asset);
        return stakingReward;
    }

    public double calculateStakingRewardValue(Asset asset) {
        double stakingRewardValue = 0.0;
        stakingRewardValue = calculateValue(calculateStakingRewardAmount(asset), asset);
        return stakingRewardValue;
    }

    public double calculateTotalValue(Asset asset) {
        double totalValue = 0.0;
        totalValue = calculateValue(getTotalAmount(asset), asset);
        return totalValue;
    }

    public double calculateValue(double amount, Asset asset) {
        double value = 0.0;
        value = amount * getTickerPrice(asset);
        return value;
    }

    public double getTickerPrice(Asset asset) {
        JSONArray response;
        double tickerPrice = 0.0;
        JSONObject json = new JSONObject();
        json.put("market", asset.getMarket());
        response = bitvavo.tickerPrice(json);
        tickerPrice = response.getJSONObject(0).getDouble("price");
        return tickerPrice;
    }

    public double calculateTotalCost(Asset asset) {
        JSONArray response;
        double totalCost = 0.0;
        double fee, filledAmountQuote;
        totalCost = 0.0;
        String side, status;
        response = bitvavo.getOrders(asset.getMarket(), new JSONObject());
        for (int i = 0; i < response.length(); i++) {
//            System.out.println(response.getJSONObject(i).toString(2));
            side = response.getJSONObject(i).getString("side");
            filledAmountQuote = response.getJSONObject(i).getDouble("filledAmountQuote");
            fee = response.getJSONObject(i).getDouble("feePaid");
            status = response.getJSONObject(i).getString("status");
            if (status.equals("filled")) {
                if (side.equals("buy")) {
                    totalCost = totalCost + filledAmountQuote + fee;
                } else if (side.equals("sell")) {
                    totalCost += fee;
                }
            }
        }
        return totalCost;
    }

    public double getValueSold(Asset asset) {
        JSONArray response;
        double filledAmountQuote, valueSold;
        valueSold = 0.0;
        String side, status;
        response = bitvavo.getOrders(asset.getMarket(), new JSONObject());
        for (int i = 0; i < response.length(); i++) {
            side = response.getJSONObject(i).getString("side");
            filledAmountQuote = response.getJSONObject(i).getDouble("filledAmountQuote");
            status = response.getJSONObject(i).getString("status");
            if (status.equals("filled")) {
                if (side.equals("sell")) {
                    valueSold += filledAmountQuote;
                }
            }
        }
        return valueSold;
    }

    public double calculateProfit(Asset asset) {
        double profit = 0.0;
        profit = calculateTotalValue(asset) + getValueSold(asset) - calculateTotalCost(asset);
        return profit;
    }

    public double calculateAveragePrice(Asset asset) {
        double averagePrice = 0.0;
        averagePrice = (calculateTotalCost(asset) / getTotalAmountBought(asset));
        return averagePrice;
    }

    public int getLimit() {
        int remainingLimit = bitvavo.getRemainingLimit();
        return remainingLimit;
    }
}

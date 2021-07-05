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

    /*public double calculateStakingReward(Asset asset) {
        double
    }*/

    public double getTickerPrice(Asset asset)
    {
        JSONArray response;
        double tickerPrice = 0.0;
        JSONObject json = new JSONObject();
        json.put("market", asset.getMarket());
        response = bitvavo.tickerPrice(json);
        tickerPrice = response.getJSONObject(0).getDouble("price"); //TEST THIS
        return tickerPrice;
    }

    /*public void testREST(Asset asset) {
        JSONArray response;

        double currentAssetPrice = 0.0;
        JSONObject json = new JSONObject();
        json.put("market", "ADA-EUR");
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
        response = bitvavo.getOrders("ADA-EUR", new JSONObject());
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

        // System.out.println(bitvavo.account().toString(2));


        int remaining = bitvavo.getRemainingLimit();
        System.out.println("remaining limit is " + remaining);
    }*/
}

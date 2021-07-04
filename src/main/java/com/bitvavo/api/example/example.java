package com.bitvavo.api.example;

import com.bitvavo.api.*;
import org.json.*;

import java.util.*;

/*
 * This is an example utilising all functions of the node Bitvavo API wrapper.
 * The APIKEY and APISECRET should be replaced by your own key and secret.
 * For public functions the APIKEY and SECRET can be removed.
 * Documentation: https://docs.bitvavo.com
 * Bitvavo: https://bitvavo.com
 * README: https://github.com/bitvavo/java-bitvavo-api
 */

public class example {
    public static void main(String args[]) {
        Bitvavo bitvavo = new Bitvavo(new JSONObject("{" +
                "APIKEY: '" + APIKeysLocal.KEY + "', " +
                "APISECRET: '" + APIKeysLocal.SECRET + "', " +
                "RESTURL: 'https://api.bitvavo.com/v2'," +
                "WSURL: 'wss://ws.bitvavo.com/v2/'," +
                "ACCESSWINDOW: 10000, " +
                "DEBUGGING: false }"));

        testREST(bitvavo);
        testWebsocket(bitvavo);
    }

    public static void testREST(Bitvavo bitvavo) {
        JSONArray response;

        /*List<String> assetList = new ArrayList<String>();
        response = bitvavo.assets(new JSONObject());
        for (int i = 0; i < response.length(); i++) {
//            System.out.println(response.getJSONObject(i).toString(2));
            assetList.add(response.getJSONObject(i).getString("symbol"));
        }
        for (String symbol : assetList)
        {
            System.out.println(symbol);
        }*/


        /*JSONObject json = new JSONObject();
        json.put("limit", "5");
        response = bitvavo.publicTrades("BTC-EUR", json);
        for (int i = 0; i < response.length(); i++) {
            System.out.println(response.getJSONObject(i).toString(2));
        }*/
        double currentAssetPrice = 0.0;
        JSONObject json = new JSONObject();
        json.put("market", "ENJ-EUR");
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
        response = bitvavo.getOrders("ENJ-EUR", new JSONObject());
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

        // response = bitvavo.balance(new JSONObject());
        // for(int i = 0; i < response.length(); i ++) {
        //   System.out.println(response.getJSONObject(i).toString(2));
        // }

        // System.out.println(bitvavo.depositAssets("BTC").toString(2));

        // System.out.println(bitvavo.withdrawAssets("BTC", "1", "BitcoinAddress", new JSONObject()).toString(2));

        // response = bitvavo.depositHistory(new JSONObject());
        // for(int i = 0; i < response.length(); i ++) {
        //   System.out.println(response.getJSONObject(i).toString(2));
        // }

        // response = bitvavo.withdrawalHistory(new JSONObject());
        // for(int i = 0; i < response.length(); i ++) {
        //   System.out.println(response.getJSONObject(i).toString(2));
        // }

        int remaining = bitvavo.getRemainingLimit();
        System.out.println("remaining limit is " + remaining);
    }

    public static void testWebsocket(Bitvavo bitvavo) {
        Bitvavo.Websocket ws = bitvavo.newWebsocket();

        ws.setErrorCallback(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(JSONObject response) {
                System.out.println("Found ERROR, own callback." + response);
            }
        });

        ws.time(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(JSONObject responseObject) {
                System.out.println(responseObject.getJSONObject("response").toString(2));
            }
        });

        // ws.markets(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.assets(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.book("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     System.out.println(responseObject.getJSONObject("response").toString(2));
        //   }
        // });

        // ws.publicTrades("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.candles("BTC-EUR", "1h", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONArray(i).toString(2));
        //     }
        //   }
        // });

        // ws.ticker24h(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.tickerPrice(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.tickerBook(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.placeOrder("BTC-EUR", "sell", "limit", new JSONObject("{ amount: 1.2, price: 6000 }"), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     System.out.println(responseObject.getJSONObject("response").toString(2));
        //   }
        // });

        // ws.updateOrder("BTC-EUR", "8653b765-f6ce-44ad-b474-8cf56bd4469f", new JSONObject("{ amount: 1.4 }"), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     System.out.println(responseObject.getJSONObject("response").toString(2));
        //   }
        // });

        // ws.getOrder("BTC-EUR", "8653b765-f6ce-44ad-b474-8cf56bd4469f", new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     System.out.println(responseObject.getJSONObject("response").toString(2));
        //   }
        // });

        // ws.cancelOrder("BTC-EUR", "8653b765-f6ce-44ad-b474-8cf56bd4469f", new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     System.out.println(responseObject.getJSONObject("response").toString(2));
        //   }
        // });

        // ws.getOrders("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.cancelOrders(new JSONObject("{ market: BTC-EUR }"), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.ordersOpen(new JSONObject("{ market: BTC-EUR }"), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.trades("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.account(new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONObject response = responseObject.getJSONObject("response");
        //     System.out.println(response.toString(2));
        //   }
        // });

        // ws.balance(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.depositAssets("BTC", new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     System.out.println(responseObject.getJSONObject("response").toString(2));
        //   }
        // });

        // ws.withdrawAssets("BTC", "1", "BitcoinAddress", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     System.out.println(responseObject.getJSONObject("response").toString(2));
        //   }
        // });

        // ws.depositHistory(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.withdrawalHistory(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject responseObject) {
        //     JSONArray response = responseObject.getJSONArray("response");
        //     for (int i = 0; i < response.length(); i ++) {
        //       System.out.println(response.getJSONObject(i).toString(2));
        //     }
        //   }
        // });

        // ws.subscriptionTicker("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject response) {
        //     System.out.println(response.toString(2));
        //   }
        // });

        // ws.subscriptionTicker24h("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject response) {
        //     System.out.println(response.toString(2));
        //   }
        // });

        // ws.subscriptionAccount("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject response) {
        //     System.out.println(response.toString(2));
        //   }
        // });

        // ws.subscriptionCandles("BTC-EUR", "1h", new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject response) {
        //     System.out.println(response.toString(2));
        //   }
        // });

        // ws.subscriptionTrades("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject response) {
        //     System.out.println(response.toString(2));
        //   }
        // });

        // ws.subscriptionBookUpdate("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
        //   public void handleMessage(JSONObject response) {
        //     System.out.println(response.toString(2));
        //   }
        // });

        // ws.subscriptionBook("BTC-EUR", new WebsocketClientEndpoint.BookHandler() {
        //   public void handleBook(Map<String, Object> book) {
        //     List<List<String>> bids = (List<List<String>>)book.get("bids");
        //     List<List<String>> asks = (List<List<String>>)book.get("asks");
        //     String nonce = (String)book.get("nonce");
        //     System.out.println(book);
        //   }
        // });

        // The following function can be used to close the socket, callbacks will no longer be called.
        // ws.close()
    }
}

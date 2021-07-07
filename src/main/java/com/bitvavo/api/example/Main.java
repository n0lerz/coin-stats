package com.bitvavo.api.example;

import com.bitvavo.api.example.models.Asset;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        BitvavoAPI bitvavoAPI = new BitvavoAPI();
//        bitvavoAPI.getAllAssets();
        Asset asset = new Asset("HOT");
        System.out.println(bitvavoAPI.getTickerPrice(asset));
        bitvavoAPI.getTotalAmount(asset);
        bitvavoAPI.printRemainingLimit();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }
}

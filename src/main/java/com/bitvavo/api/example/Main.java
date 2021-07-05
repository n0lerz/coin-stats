package com.bitvavo.api.example;

import com.bitvavo.api.example.models.Asset;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        BitvavoAPI bitvavoAPI = new BitvavoAPI();
        bitvavoAPI.getAllAssets();
        Asset asset = new Asset("ENJ");
        System.out.println(bitvavoAPI.getTickerPrice(asset));
        bitvavoAPI.printAssetStats(asset);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }
}

package com.bitvavo.api.example.views;

import com.bitvavo.api.example.service.BitvavoAPI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AssetView {
    public AssetView(BitvavoAPI bitvavoAPI) throws Exception {
        /*BitvavoAPI bitvavoAPI = new BitvavoAPI();
//        bitvavoAPI.getAllAssets();
        Asset asset = new Asset("HOT");
        System.out.println(bitvavoAPI.getTickerPrice(asset));
        bitvavoAPI.getTotalAmount(asset);
        bitvavoAPI.printRemainingLimit();*/

        Parent root = FXMLLoader.load(getClass().getResource("/assetView.fxml"));

        Scene scene = new Scene(root, 600, 400);

        Stage stage = new Stage();

        stage.setTitle("Overview");
        stage.setScene(scene);
        stage.show();
    }
}

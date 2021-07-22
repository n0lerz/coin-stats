package com.bitvavo.api.example.controllers;

import com.bitvavo.api.example.models.Asset;
import com.bitvavo.api.example.service.BitvavoAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Observable;

public class AssetController {

    private final Stage thisStage;
    @FXML
    private ListView assetsListView;
    @FXML
    private Label profitLabel;
    private BitvavoAPI bitvavoAPI;

    public AssetController(MainController mainController, BitvavoAPI bitvavoAPI) {
        thisStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assetView.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            thisStage.setScene(scene);
            thisStage.setTitle("Overview");
            this.bitvavoAPI = bitvavoAPI;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        thisStage.showAndWait();
    }

    @FXML
    private void initialize() {
        ObservableList<Asset> assetsList = FXCollections.observableArrayList(bitvavoAPI.getAllOwnedAssets());
        assetsListView.setItems(assetsList);
    }
//
//    private void openLayout2() {
//        C
//    }
}

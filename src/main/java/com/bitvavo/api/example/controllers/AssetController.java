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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Observable;

public class AssetController {

    @FXML
    private ListView<Asset> assetsListView;
    @FXML
    private Label profitLabel;

    private BitvavoAPI bitvavoAPI;


    /*@FXML
    private void initialize() {
        ObservableList<Asset> assetsList = FXCollections.observableArrayList(bitvavoAPI.getAllOwnedAssets());
        assetsListView.setItems(assetsList);
    }*/

    public void populateAssetListView(BitvavoAPI bitvavoAPI) {
        this.bitvavoAPI = bitvavoAPI;
        ObservableList<Asset> assetsList = FXCollections.observableArrayList(bitvavoAPI.getAllOwnedAssets());
        assetsListView.setItems(assetsList);
        assetsListView.setCellFactory(param -> new ListCell<Asset>() {
            @Override
            protected void updateItem(Asset item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getSymbol() == null) {
                    setText(null);
                } else {
                    setText(item.getSymbol());
                }
            }
        });
    }



//
//    private void openLayout2() {
//        C
//    }
}

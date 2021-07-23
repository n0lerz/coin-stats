package com.bitvavo.api.example.controllers;

import com.bitvavo.api.example.models.Asset;
import com.bitvavo.api.example.service.BitvavoAPI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class AssetController {

    @FXML
    private ListView<Asset> assetsListView;
    @FXML
    private Label labelTickerPrice;
    @FXML
    private Label labelTotalAmount;
    @FXML
    private Label labelTotalValue;
    @FXML
    private Label labelTotalCost;
    @FXML
    private Label labelValueSold;
    @FXML
    private Label labelProfit;
    @FXML
    private Label labelStakingAmount;
    @FXML
    private Label labelStakingValue;
    @FXML
    private Label labelAveragePrice;


    private BitvavoAPI bitvavoAPI;


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

        assetsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Asset>() {

            @Override
            public void changed(ObservableValue<? extends Asset> observable, Asset oldValue, Asset selectedAsset) {
                selectedAsset.setAmount(bitvavoAPI.getTotalAmount(selectedAsset));
                selectedAsset.setCurrentTickerPrice(bitvavoAPI.getTickerPrice(selectedAsset));
                selectedAsset.setAmount(bitvavoAPI.getTotalAmount(selectedAsset));
                selectedAsset.setTotalValue(bitvavoAPI.calculateTotalValue(selectedAsset));
                selectedAsset.setTotalCost(bitvavoAPI.calculateTotalCost(selectedAsset));
                selectedAsset.setValueSold(bitvavoAPI.getValueSold(selectedAsset));
                selectedAsset.setProfit(bitvavoAPI.calculateProfit(selectedAsset));
                selectedAsset.setStakingRewardAmount(bitvavoAPI.calculateStakingRewardAmount(selectedAsset));
                selectedAsset.setStakingRewardValue(bitvavoAPI.calculateStakingRewardValue(selectedAsset));
                selectedAsset.setAveragePrice(bitvavoAPI.calculateAveragePrice(selectedAsset));

                labelTickerPrice.setText("€" + selectedAsset.getCurrentTickerPrice());
                labelTotalAmount.setText(selectedAsset.getAmount() + " " + selectedAsset.getSymbol());
                labelTotalValue.setText("€" + selectedAsset.getTotalValue());
                labelTotalCost.setText("€" + selectedAsset.getTotalCost());
                labelValueSold.setText("€" + selectedAsset.getValueSold());
                labelProfit.setText("€" + selectedAsset.getProfit());
                labelStakingAmount.setText(selectedAsset.getStakingRewardAmount() + " " + selectedAsset.getSymbol());
                labelStakingValue.setText("€" + selectedAsset.getStakingRewardValue());
                labelAveragePrice.setText("€" + selectedAsset.getAveragePrice());

                bitvavoAPI.printRemainingLimit();
            }
        });
    }
}

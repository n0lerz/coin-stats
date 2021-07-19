package com.bitvavo.api.example.controllers;

import com.bitvavo.api.example.service.BitvavoAPI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private Button button;
    @FXML
    private TextField apiKey, apiSecret;
    @FXML
    private String key, secret;

    @FXML
    public void handleButtonClick() {
        try {
            if ((!apiKey.getText().isEmpty()) && (!apiSecret.getText().isEmpty()) && (apiKey.getText().length() == 64)) {
                key = apiKey.getText();
                secret = apiSecret.getText();
                BitvavoAPI bitvavoAPI = new BitvavoAPI(key, secret);
                if (!bitvavoAPI.getAllOwnedAssets().isEmpty()) {
                    openAssetView(bitvavoAPI);
                    Stage stage = (Stage) button.getScene().getWindow();
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login failed");
                    alert.setHeaderText("Invalid API Key and/or Secret");
                    alert.setContentText("Please try again.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login failed");
                alert.setHeaderText("Invalid API Key and/or Secret");
                alert.setContentText("Please try again.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  @FXML
    private void initialize() {
        // Add an action for the "Open Layout2" button
        button.setOnAction(event -> openAssetView(bitvavo));
    }*/

    private void openAssetView(BitvavoAPI bitvavoAPI) {
        AssetController assetController = new AssetController(this, bitvavoAPI);
        assetController.showStage();
    }
}

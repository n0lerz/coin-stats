package com.bitvavo.api.example.controllers;

import com.bitvavo.api.example.service.BitvavoAPI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
                /*Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login failed");
                alert.setHeaderText("Invalid API Key and/or Secret");
                alert.setContentText("Please try again.");
                alert.showAndWait();*/
                BitvavoAPI bitvavoAPIKeylessEntry = new BitvavoAPI();
                openAssetView(bitvavoAPIKeylessEntry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openAssetView(BitvavoAPI bitvavoAPI) {
        Stage thisStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assetView.fxml"));
            Parent root = loader.load();
            AssetController assetController = loader.getController();
            assetController.populateAssetListView(bitvavoAPI);
//            loader.setController(this);
            Scene scene = new Scene(root, 600, 400);
            thisStage.setScene(scene);
            thisStage.setTitle("Overview");
            thisStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

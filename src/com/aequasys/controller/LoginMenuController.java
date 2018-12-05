package com.aequasys.controller;

import com.aequasys.eventsClasses.ErrorLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginMenuController {
    public Button btn_connect;

    private void logError(Exception e){
        ErrorLogger errorLogger = new ErrorLogger();
        errorLogger.log(e);
    }

    public void connect_btn_pressed(ActionEvent event) {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/MainMenu.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            Stage stage = new Stage();
            stage.setTitle("CV Generator");
            stage.setScene(new Scene(root2));
            stage.show();
            closeWindow();

        } catch (IOException e) {
            logError(e);
        }
    }

    public void btn_settings_pressed(ActionEvent event) {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/Settings.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            SettingsController settingsController = fxmlloader.getController();
            settingsController.init_data();
            Stage stage = new Stage();
            stage.setTitle("Connection settings");
            stage.setScene(new Scene(root2));
            stage.showAndWait();

        } catch (IOException e) {
            logError(e);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btn_connect.getScene().getWindow();
        stage.close();
    }
}

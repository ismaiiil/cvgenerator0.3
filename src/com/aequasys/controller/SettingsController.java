package com.aequasys.controller;

import com.aequasys.eventsClasses.ErrorLogger;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class SettingsController {
    public Button cancel_btn;
    public Button save_btn;
    public TextField ip_text;
    //public TextField port_text;
    public TextField sid_text;
    public TextField username_text;
    public TextField password_text;
    private Preferences preferences = Preferences.userRoot();

    public void cancel_btn_pressed(ActionEvent event) {
        closeWindow();
    }
    private void closeWindow() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

    public void init_data(){
        ip_text.setText(preferences.get("ip",""));
        //port_text.setText(preferences.get("port",""));
        sid_text.setText(preferences.get("sid",""));
        username_text.setText(preferences.get("username",""));
        password_text.setText(preferences.get("password",""));



    }
    public void save_btn_pressed(ActionEvent event) {

        preferences.put("ip",ip_text.getText());
        //preferences.put("port",port_text.getText());
        preferences.put("sid",sid_text.getText());
        preferences.put("username",username_text.getText());
        preferences.put("password",password_text.getText());
        closeWindow();

    }
}

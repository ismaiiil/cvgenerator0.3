package com.aequasys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/aequasys/view/loginMenu.fxml"));
        primaryStage.setTitle("CV generator");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {

        launch(args);
    }
}

/*
    TODONE: give user option to choose destination of CV

    TODONE: give user option to choose last 5,4,3... etc years of experience

    TODONE: filter users by masteries in MainMenu.fxml
        -TODONE: maybe filter users by name and surname too
        -TOSKIP: find SQL for multiple qualifications and code it in JDBCs
        -TODONE: do filtering application side instead of server side

    TODO: create new template, (optional: add icons for email and location)
    TODO: create a menu where the user cna establish the connection with settings
    TODONE: give user option to user another template

    TODONE: fix crash on wrong IP
 */
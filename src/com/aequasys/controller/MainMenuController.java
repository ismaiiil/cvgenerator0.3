package com.aequasys.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.aequasys.eventsClasses.Column;
import com.aequasys.eventsClasses.HeaderFooterPageEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import com.aequasys.model.dao.jdbc.*;
import com.aequasys.model.vo.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    public Button button_refresh;
    public TableColumn col_id;
    public TableColumn col_name;
    public TableColumn col_surname;
    public TableColumn col_email;
    public TableColumn col_position;
    public Button add_btn;
    public Button edit_btn;
    public Button all_details_btn;
    public Button generate_cv_button;
    public Button button_settings;
    public TextField name_search_box;
    public TextField surname_search_box;

    @FXML
    private Button delete_btn;

    @FXML
    private TableView table_object;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col_id.setCellValueFactory(
                new PropertyValueFactory<User,Integer>("id")
        );
        col_name.setCellValueFactory(
                new PropertyValueFactory<User,String>("name")
        );
        col_surname.setCellValueFactory(
                new PropertyValueFactory<User,String>("surname")
        );
        col_email.setCellValueFactory(
                new PropertyValueFactory<User,String>("email")
        );
        col_position.setCellValueFactory(
                new PropertyValueFactory<User,String>("position")
        );
        Column.setCellWrappable(col_email);
        Column.setCellWrappable(col_surname);
        //refreshTable();


    }


    public void refreshTable() {
        JDBCUserDao jdbcUserDao = new JDBCUserDao();
        table_object.setItems(jdbcUserDao.select());
    }


    public void button_refresh_pressed(ActionEvent event) {
        refreshTable();

    }

    public void delete_btn_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are sure you want to delete this user?");
        alert.setContentText("Delete? You will also lose all data related to this user.");
        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            JDBCUserDao jdbcUserDao = new JDBCUserDao();
            if(user != null){
                jdbcUserDao.delete(user.getId());
                refreshTable();
            }
        }


    }

    public void add_btn_pressed(ActionEvent event) {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/UserBasicDetails.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlloader.load();
            UserBasicDetailsController userBasicDetailsController = fxmlloader.getController();
            Stage stage = new Stage();
            stage.setTitle("Add a User");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.showAndWait();
            if(userBasicDetailsController.button_pressed){
                refreshTable();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void edit_btn_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        if (user != null) {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/UserBasicDetails.fxml"));
            try {
                Parent root2 = (Parent) fxmlloader.load();
                UserBasicDetailsController userBasicDetailsController = fxmlloader.getController();
                userBasicDetailsController.init_user_details(user);
                userBasicDetailsController.initdata();
                Stage stage = new Stage();
                stage.setTitle("Edit User:" + user.getName() + user.getSurname());
                stage.setScene(new Scene(root2));
                //stage.setResizable(false);
                stage.showAndWait();
                if(userBasicDetailsController.button_pressed){
                    refreshTable();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    public void all_details_btn_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        if (user != null) {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/UserRecordsDetails.fxml"));
            try {
                Parent root2 = (Parent) fxmlloader.load();
                UserRecordsDetailsController userRecordsDetailsController = fxmlloader.getController();
                userRecordsDetailsController.initUserId(user);
                Stage stage = new Stage();
                stage.setTitle("Edit User:" + user.getName() + user.getSurname());
                stage.setScene(new Scene(root2));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    public void generate_cv_button_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/generateCV.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlloader.load();
            GenerateCVController generateCVController = fxmlloader.getController();
            generateCVController.init_user(user);
            Stage stage = new Stage();
            stage.setTitle("Generate CV for user: "+ user.getName()+" "+ user.getSurname());
            stage.setScene(new Scene(root1));
            stage.showAndWait();
            if(generateCVController.button_pressed){
                refreshTable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }

    public void button_settings_pressed(ActionEvent event) {
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
            e.printStackTrace();
        }
    }

    public void search_button_pressed(ActionEvent event) {
        JDBCUserDao jdbcUserDao = new JDBCUserDao();
        table_object.setItems(jdbcUserDao.selectByNameSurname(name_search_box.getText(),surname_search_box.getText()));
    }
}
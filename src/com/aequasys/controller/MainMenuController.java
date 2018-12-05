package com.aequasys.controller;

import com.aequasys.eventsClasses.ErrorLogger;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.aequasys.eventsClasses.Column;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.aequasys.model.dao.jdbc.*;
import com.aequasys.model.vo.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

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
    public TextField name_search_box;
    public TextField surname_search_box;
    public ChoiceBox choicebox_masteries;
    public TextField textfield_filters_display;
    public ChoiceBox choicebox_remove_masteries;

    @FXML
    private Button delete_btn;

    @FXML
    private TableView table_object;

    private List<String> stringListOfFilters = new ArrayList<String>();

    private List<String> selectedStringListOfFilters = new ArrayList<String>();

    private void logError(Exception e){
        ErrorLogger errorLogger = new ErrorLogger();
        errorLogger.log(e);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        resetFilters();
        initTableCells();
        refreshFilterChoiceBox();
        textfield_filters_display.setEditable(false);
    }

    private void resetFilters() {
        stringListOfFilters.clear();
        selectedStringListOfFilters.clear();
        textfield_filters_display.clear();
        JDBCFilterDao jdbcFilterDao = new JDBCFilterDao();
        for (Filter filter:jdbcFilterDao.select()) {
            stringListOfFilters.add(filter.getMastery());
        }
        refreshTable();
    }

    private void initTableCells() {
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
    }

    private void refreshFilterChoiceBox() {
        ObservableList observableListOfFilters = FXCollections.observableArrayList();

        for (String filter:stringListOfFilters) {
            observableListOfFilters.add(filter);
        }

        choicebox_masteries.setItems(observableListOfFilters);
        choicebox_masteries.getSelectionModel().selectFirst();
    }

    private void refreshRemoveFilterChoiceBox(){
        ObservableList removeObservableListOfFilters = FXCollections.observableArrayList();
        for (String filter:selectedStringListOfFilters) {
            removeObservableListOfFilters.add(filter);
        }

        choicebox_remove_masteries.setItems(removeObservableListOfFilters);
        choicebox_remove_masteries.getSelectionModel().selectFirst();
    }

    public void refreshTable() {
        JDBCUserDao jdbcUserDao = new JDBCUserDao();
        table_object.setItems(jdbcUserDao.select());
    }

    public void refreshFilterTexfield(){
        textfield_filters_display.clear();
        for (String filter:selectedStringListOfFilters) {
            textfield_filters_display.appendText(filter+", ");
        }
    }

    public void button_refresh_pressed(ActionEvent event) {
        refreshTable();
        resetFilters();
        refreshFilterChoiceBox();
        refreshRemoveFilterChoiceBox();
    }

    public void delete_btn_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        if (user !=null){
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
            logError(e);
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
                logError(e);
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
                stage.showAndWait();
                resetFilters();
                refreshFilterChoiceBox();
                refreshRemoveFilterChoiceBox();
            } catch (IOException e) {
                logError(e);
            }


        }

    }

    public void generate_cv_button_pressed(ActionEvent event) {
        User user = (User) table_object.getSelectionModel().getSelectedItem();
        if (user != null ){
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
                logError(e);
            }
        }

    }

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }



    public void search_button_pressed(ActionEvent event) {
        JDBCUserDao jdbcUserDao = new JDBCUserDao();
        table_object.setItems(jdbcUserDao.selectByNameSurname(name_search_box.getText(),surname_search_box.getText()));
    }

    public void btn_add_filter_pressed(ActionEvent event) {
        for (String filter:stringListOfFilters) {
            if(choicebox_masteries.getValue() == filter){
                selectedStringListOfFilters.add(filter);
                stringListOfFilters.remove(filter);
                refreshRemoveFilterChoiceBox();
                refreshFilterChoiceBox();
                refreshFilterTexfield();
                break;
            }
        }


    }

    public void btn_apply_filters_pressed(ActionEvent event) {
        ObservableList observableListofFilteredUsers = FXCollections.observableArrayList();
        ObservableList observableListofMasteries = FXCollections.observableArrayList();
        JDBCUserDao jdbcUserDao = new JDBCUserDao();
        JDBCMasteryDao jdbcMasteryDao = new JDBCMasteryDao();
        for (User user:jdbcUserDao.select()) {
            observableListofMasteries.clear();
            for (Mastery mastery:jdbcMasteryDao.select(user.getId())) {
                observableListofMasteries.add(mastery.getTechnology());
            }
            if(observableListofMasteries.containsAll(selectedStringListOfFilters)){
                observableListofFilteredUsers.add(user);
            }
        }
        table_object.setItems(observableListofFilteredUsers);
    }

    public void btn_remove_filter_pressed(ActionEvent event) {
        for (String filter:selectedStringListOfFilters) {
            if(choicebox_remove_masteries.getValue() == filter){
                stringListOfFilters.add(filter);
                selectedStringListOfFilters.remove(filter);
                refreshRemoveFilterChoiceBox();
                refreshFilterChoiceBox();
                refreshFilterTexfield();
                break;
            }
        }
    }

    public void btn_stored_filters_pressed(ActionEvent event) {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/FiltersDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            Stage stage = new Stage();
            stage.setTitle("Filters stored");
            stage.setScene(new Scene(root2));
            stage.showAndWait();
            resetFilters();
            refreshFilterChoiceBox();
            refreshRemoveFilterChoiceBox();

        } catch (IOException e) {
            logError(e);
        }
    }
}
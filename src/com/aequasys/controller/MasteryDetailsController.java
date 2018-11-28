package com.aequasys.controller;

import com.aequasys.model.dao.jdbc.JDBCFilterDao;
import com.aequasys.model.vo.Filter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.aequasys.model.dao.jdbc.JDBCMasteryDao;
import com.aequasys.model.vo.Mastery;

import java.io.IOException;

public class MasteryDetailsController {
    public Button cancel_btn;
    public Button save_btn;
    public TextField years_text;
    public ChoiceBox choicebox_mastery;

    private Mastery passedMastery;

    boolean button_pressed;

    public void cancel_btn_pressed(ActionEvent event) {
        button_pressed = true;
        closeWindow();
    }

    public void save_btn_pressed(ActionEvent event) {
        commitMastery();
    }

    public void init_mastery(Mastery mastery){
        passedMastery = mastery;
        resetChoicebox();
        if(passedMastery.getId()!=0){
            years_text.setText(String.valueOf(passedMastery.getExperience()));
            choicebox_mastery.setValue(passedMastery.getTechnology());
        }
    }

    private void resetChoicebox() {
        choicebox_mastery.getItems().clear();
        JDBCFilterDao jdbcFilterDao = new JDBCFilterDao();
        ObservableList filters = FXCollections.observableArrayList();
        for (Filter filter:jdbcFilterDao.select()) {
            filters.add(filter.getMastery());
        }
        choicebox_mastery.setItems(filters);
    }

    private void commitMastery(){
        Mastery mastery = new Mastery();
        mastery.setUser_id(passedMastery.getUser_id());
        mastery.setExperience(Integer.parseInt(years_text.getText()));
        mastery.setTechnology(choicebox_mastery.getValue().toString());
        JDBCMasteryDao jdbcMasteryDao = new JDBCMasteryDao();
        if(passedMastery.getId()==0){
            jdbcMasteryDao.insert(mastery);
        }else{
            mastery.setId(passedMastery.getId());
            jdbcMasteryDao.update(mastery);
        }
        button_pressed = true;
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

    public void add_options_btn_pressed(ActionEvent event) {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/FiltersDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            Stage stage = new Stage();
            stage.setTitle("Filters stored");
            stage.setScene(new Scene(root2));
            stage.showAndWait();
            resetChoicebox();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
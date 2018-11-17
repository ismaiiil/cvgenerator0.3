package com.aequasys.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.aequasys.model.dao.jdbc.JDBCMasteryDao;
import com.aequasys.model.vo.Mastery;

public class MasteryDetailsController {
    public Button cancel_btn;
    public Button save_btn;
    public TextField years_text;
    public TextArea technology_text;

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
        if(passedMastery.getId()!=0){
            years_text.setText(String.valueOf(passedMastery.getExperience()));
            technology_text.setText(passedMastery.getTechnology());
        }
    }

    private void commitMastery(){
        Mastery mastery = new Mastery();
        mastery.setUser_id(passedMastery.getUser_id());
        mastery.setExperience(Integer.parseInt(years_text.getText()));
        mastery.setTechnology(technology_text.getText());
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
}

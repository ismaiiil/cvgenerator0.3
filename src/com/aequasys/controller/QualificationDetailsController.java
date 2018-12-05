package com.aequasys.controller;

import com.aequasys.eventsClasses.IntField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.aequasys.model.dao.jdbc.JDBCQualificationDao;
import com.aequasys.model.vo.Qualification;

public class QualificationDetailsController {
    public TextField year_text;
    public TextField qualification_text;
    public TextField university_text;
    public Button cancel_btn;
    public Button save_btn;

    private Qualification passedQualification;

    boolean button_pressed;

    public void init_qualification(Qualification qualification){
        passedQualification = qualification;
        if(passedQualification.getId()!=0){
            year_text.setText(String.valueOf(passedQualification.getYear()));
            qualification_text.setText(passedQualification.getDiploma());
            university_text.setText(passedQualification.getUniversity());

        }
        // force the field to be numeric only
        IntField.convertToIntField(year_text);
    }

    public void cancel_btn_pressed(ActionEvent event) {
        button_pressed = true;
        closeWindow();
    }

    public void save_btn_pressed(ActionEvent event) {
        if(!year_text.getText().equals("")){
            Qualification qualification = new Qualification();
            qualification.setUser_id(passedQualification.getUser_id());
            qualification.setYear(Integer.parseInt(year_text.getText()));
            qualification.setDiploma(qualification_text.getText());
            qualification.setUniversity(university_text.getText());
            JDBCQualificationDao jdbcQualificationDao = new JDBCQualificationDao();
            if(passedQualification.getId() == 0){
                jdbcQualificationDao.insert(qualification);
            }else{
                qualification.setId(passedQualification.getId());
                jdbcQualificationDao.update(qualification);
            }
            button_pressed = true;
            closeWindow();
        }


    }
    private void closeWindow() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}

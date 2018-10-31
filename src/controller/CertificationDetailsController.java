package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dao.jdbc.JDBCCertificationDao;
import model.vo.Certification;

public class CertificationDetailsController {
    public Button cancel_btn;
    public Button save_btn;
    public TextField year_text;
    public TextArea certification_text;

    private Certification passedCertification;

    boolean button_pressed;

    public void cancel_btn_pressed(ActionEvent event) {
        button_pressed = true;
        closeWindow();
    }

    public void save_btn_pressed(ActionEvent event) {
        if(passedCertification.getId() == 0){
            Certification certification = new Certification();
            certification.setUser_id(passedCertification.getUser_id());
            certification.setYear(Integer.parseInt(year_text.getText()));
            certification.setQualification(certification_text.getText());
            JDBCCertificationDao jdbcCertificationDao = new JDBCCertificationDao();
            jdbcCertificationDao.insert(certification);
            button_pressed = true;
            closeWindow();

        }else{
            Certification certification = new Certification();
            certification.setUser_id(passedCertification.getUser_id());
            certification.setId(passedCertification.getId());
            certification.setYear(Integer.parseInt(year_text.getText()));
            certification.setQualification(certification_text.getText());
            JDBCCertificationDao jdbcCertificationDao = new JDBCCertificationDao();
            jdbcCertificationDao.update(certification);
            button_pressed = true;
            closeWindow();
        }

    }

    public void init_certification(Certification certification) {
        passedCertification = certification;
        if(passedCertification.getId()!= 0){
            year_text.setText(String.valueOf(passedCertification.getYear()));
            certification_text.setText(certification.getQualification());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}

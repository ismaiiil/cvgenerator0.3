package com.aequasys.controller;

import com.aequasys.eventsClasses.Column;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.aequasys.model.dao.jdbc.JDBCCertificationDao;
import com.aequasys.model.dao.jdbc.JDBCMasteryDao;
import com.aequasys.model.dao.jdbc.JDBCQualificationDao;
import com.aequasys.model.dao.jdbc.JDBCReportDao;
import com.aequasys.model.vo.*;

import java.io.IOException;

public class UserRecordsDetailsController{
    @FXML
    public Button button_refresh;

    //certification TiltedPane
    @FXML
    public TableView certification_table;
    public Button certification_add_btn;
    public Button certification_delete_btn;
    public Button certification_edit_btn;
    public TableColumn certification_column;
    public TableColumn certification_year_column;

    //Masteries TiltedPane
    @FXML
    public TableView masteries_table;
    public Button masteries_add_btn;
    public Button masteries_delete_btn;
    public Button masteries_edit_btn;
    public TableColumn technologies_column;
    public TableColumn experience_column;

    //Qualifications TiltedPane
    @FXML
    public TableView qualifications_table;
    public Button qualification_add_btn;
    public Button qualification_delete_btn;
    public Button qualification_edit_btn;
    public TableColumn qualification_year_column;
    public TableColumn qualification_qualification_column;
    public TableColumn qualification_university_column;


    //Reports TiltedPane
    @FXML
    public TableView reports_table;
    public TableColumn country_column;
    public TableColumn reports_year_column;
    public TableColumn projectdetails_column;
    public Button report_add_btn;
    public Button report_delete_btn;
    public Button report_edit_btn;

    @FXML
    public Button finish_button;


    private int user_id;

    public void button_refresh_pressed(ActionEvent event) {
        refreshTable(certification_table);
        refreshTable(qualifications_table);
        refreshTable(masteries_table);
        refreshTable(reports_table);
    }

    public void initUserId(User user){
        user_id = user.getId();

        initCertificationTable();
        initQualificationTable();
        initMasteryTable();
        initReportTable();


    }

    private void refreshTable(TableView tableView) {
        if(tableView == certification_table){
            JDBCCertificationDao jdbcCertificationDao = new JDBCCertificationDao();
            certification_table.setItems(jdbcCertificationDao.select(user_id));
        }
        else if(tableView == qualifications_table){
            JDBCQualificationDao jdbcQualificationDao = new JDBCQualificationDao();
            qualifications_table.setItems(jdbcQualificationDao.select(user_id));
        }else if(tableView == masteries_table){
            JDBCMasteryDao jdbcMasteryDao = new JDBCMasteryDao();
            masteries_table.setItems(jdbcMasteryDao.select(user_id));
        }else if(tableView == reports_table){
            JDBCReportDao jdbcReportDao = new JDBCReportDao();
            reports_table.setItems(jdbcReportDao.select(user_id));
        }

    }

    private void initQualificationTable() {
        qualification_year_column.setCellValueFactory(
                new PropertyValueFactory<Qualification,Integer>("year")
        );
        qualification_qualification_column.setCellValueFactory(
                new PropertyValueFactory<Qualification,String>("diploma")
        );
        qualification_university_column.setCellValueFactory(
                new PropertyValueFactory<Qualification,String>("university")
        );
        Column.setCellWrappable(qualification_qualification_column);
        Column.setCellWrappable(qualification_university_column);
        refreshTable(qualifications_table);
    }

    private void initCertificationTable() {
        certification_column.setCellValueFactory(
                new PropertyValueFactory<Certification,String>("Qualification")
        );
        certification_year_column.setCellValueFactory(
                new PropertyValueFactory<Certification,String>("year")
        );
        refreshTable(certification_table);
        Column.setCellWrappable(certification_column);
    }

    private void initMasteryTable(){
        experience_column.setCellValueFactory(
                new PropertyValueFactory<Mastery,Integer>("experience")
        );
        technologies_column.setCellValueFactory(
                new PropertyValueFactory<Mastery,String>("technology")
        );
        refreshTable(masteries_table);
        Column.setCellWrappable(technologies_column);
    }

    private void initReportTable(){
        reports_year_column.setCellValueFactory(
                new PropertyValueFactory<Report,Integer>("Year")
        );
        country_column.setCellValueFactory(
                new PropertyValueFactory<Report,String>("country")
        );
        projectdetails_column.setCellValueFactory(
                new PropertyValueFactory<Report,String>("details")
        );
        Column.setCellWrappable(projectdetails_column);
        refreshTable(reports_table);
    }

    public void certification_add_btn_pressed(ActionEvent event) {
        Certification certification = new Certification();
        certification.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/CertificationDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            CertificationDetailsController certificationDetailsController = fxmlloader.getController();
            certificationDetailsController.init_certification(certification);
            Stage stage = new Stage();
            stage.setTitle("Adding a certification:");
            stage.setScene(new Scene(root2));
            //stage.setResizable(false);
            stage.showAndWait();
            if(certificationDetailsController.button_pressed){
                refreshTable(certification_table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void certification_delete_btn_pressed(ActionEvent event) {
        Certification certification = (Certification) certification_table.getSelectionModel().getSelectedItem();
        JDBCCertificationDao jdbcCertificationDao = new JDBCCertificationDao();
        jdbcCertificationDao.delete(certification.getId());
        refreshTable(certification_table);
    }

    public void certification_edit_btn_pressed(ActionEvent event) {
        Certification certification = (Certification) certification_table.getSelectionModel().getSelectedItem();
        certification.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/CertificationDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            CertificationDetailsController certificationDetailsController = fxmlloader.getController();
            certificationDetailsController.init_certification(certification);
            Stage stage = new Stage();
            stage.setTitle("editing certification:");
            stage.setScene(new Scene(root2));
            //stage.setResizable(false);
            stage.showAndWait();
            if(certificationDetailsController.button_pressed){
                refreshTable(certification_table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void qualification_add_btn_pressed(ActionEvent event) {
        Qualification qualification = new Qualification();
        qualification.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/QualificationDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            QualificationDetailsController qualificationDetailsController = fxmlloader.getController();
            qualificationDetailsController.init_qualification(qualification);
            Stage stage = new Stage();
            stage.setTitle("Adding a qualification:");
            stage.setScene(new Scene(root2));
            //stage.setResizable(false);
            stage.showAndWait();
            if(qualificationDetailsController.button_pressed){
                refreshTable(qualifications_table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void qualification_delete_btn_pressed(ActionEvent event) {
        Qualification qualification = (Qualification) qualifications_table.getSelectionModel().getSelectedItem();
        JDBCQualificationDao jdbcQualificationDao = new JDBCQualificationDao();
        jdbcQualificationDao.delete(qualification.getId());
        refreshTable(qualifications_table);
    }

    public void qualification_edit_btn_pressed(ActionEvent event) {
        Qualification qualification = (Qualification) qualifications_table.getSelectionModel().getSelectedItem();
        qualification.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/QualificationDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            QualificationDetailsController qualificationDetailsController = fxmlloader.getController();
            qualificationDetailsController.init_qualification(qualification);
            Stage stage = new Stage();
            stage.setTitle("Editing a qualification:");
            stage.setScene(new Scene(root2));
            //stage.setResizable(false);
            stage.showAndWait();
            if(qualificationDetailsController.button_pressed){
                refreshTable(qualifications_table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void masteries_add_btn_pressed(ActionEvent event) {
        Mastery mastery = new Mastery();
        mastery.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/MasteryDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            MasteryDetailsController masteryDetailsController = fxmlloader.getController();
            masteryDetailsController.init_mastery(mastery);
            Stage stage = new Stage();
            stage.setTitle("Adding a mastery:");
            stage.setScene(new Scene(root2));
            //stage.setResizable(false);
            stage.showAndWait();
            if(masteryDetailsController.button_pressed){
                refreshTable(masteries_table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void masteries_delete_btn_pressed(ActionEvent event) {
        Mastery mastery = (Mastery) masteries_table.getSelectionModel().getSelectedItem();
        JDBCMasteryDao jdbcMasteryDao = new JDBCMasteryDao();
        jdbcMasteryDao.delete(mastery.getId());
        refreshTable(masteries_table);
    }

    public void masteries_edit_btn_pressed(ActionEvent event) {
        Mastery mastery = (Mastery) masteries_table.getSelectionModel().getSelectedItem();
        mastery.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/MasteryDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            MasteryDetailsController masteryDetailsController = fxmlloader.getController();
            masteryDetailsController.init_mastery(mastery);
            Stage stage = new Stage();
            stage.setTitle("editing mastery:");
            stage.setScene(new Scene(root2));
            //stage.setResizable(false);
            stage.showAndWait();
            if(masteryDetailsController.button_pressed){
                refreshTable(masteries_table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void report_add_btn_pressed(ActionEvent event) {
        Report report = new Report();
        report.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/ReportDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            ReportDetailsController reportDetailsController = fxmlloader.getController();
            reportDetailsController.init_report(report);
            Stage stage = new Stage();
            stage.setTitle("Adding a report:");
            stage.setScene(new Scene(root2));
            //stage.setResizable(false);
            stage.showAndWait();
            if(reportDetailsController.button_pressed){
                refreshTable(reports_table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void report_delete_btn_pressed(ActionEvent event) {
        Report report = (Report) reports_table.getSelectionModel().getSelectedItem();
        JDBCReportDao jdbcReportDao = new JDBCReportDao();
        jdbcReportDao.delete(report.getId());
        refreshTable(reports_table);
    }

    public void report_edit_btn_pressed(ActionEvent event) {
        Report report = (Report) reports_table.getSelectionModel().getSelectedItem();
        report.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/aequasys/view/DetailsViews/ReportDetails.fxml"));
        try {
            Parent root2 = (Parent) fxmlloader.load();
            ReportDetailsController reportDetailsController = fxmlloader.getController();
            reportDetailsController.init_report(report);
            Stage stage = new Stage();
            stage.setTitle("editing a report:");
            stage.setScene(new Scene(root2));
            //stage.setResizable(false);
            stage.showAndWait();
            if(reportDetailsController.button_pressed){
                refreshTable(reports_table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finish_button_pressed(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) finish_button.getScene().getWindow();
        stage.close();
    }
}

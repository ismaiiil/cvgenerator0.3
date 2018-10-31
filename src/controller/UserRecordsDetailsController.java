package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.dao.jdbc.JDBCCertificationDao;
import model.dao.jdbc.JDBCQualificationDao;
import model.vo.Certification;
import model.vo.Qualification;
import model.vo.User;

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


    private int user_id;

    public void button_refresh_pressed(ActionEvent event) {
        refreshTable(certification_table);
        refreshTable(qualifications_table);
        refreshTable(masteries_table);
    }


    public void initUserId(User user){
        user_id = user.getId();

        initCertificationTable();
        initQualificationTable();


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


    public void certification_add_btn_pressed(ActionEvent event) {
        Certification certification = new Certification();
        certification.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/DetailsViews/CertificationDetails.fxml"));
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
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/DetailsViews/CertificationDetails.fxml"));
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


    private void refreshTable(TableView tableView) {
        if(tableView == certification_table){
            JDBCCertificationDao jdbcCertificationDao = new JDBCCertificationDao();
            certification_table.setItems(jdbcCertificationDao.select(user_id));
        }
        else if(tableView == qualifications_table){
            JDBCQualificationDao jdbcQualificationDao = new JDBCQualificationDao();
            qualifications_table.setItems(jdbcQualificationDao.select(user_id));
        }

    }


    public void qualification_add_btn_pressed(ActionEvent event) {
        Qualification qualification = new Qualification();
        qualification.setUser_id(user_id);
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/DetailsViews/QualificationDetails.fxml"));
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
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/DetailsViews/QualificationDetails.fxml"));
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
}

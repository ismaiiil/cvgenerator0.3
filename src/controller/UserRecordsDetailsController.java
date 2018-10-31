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
import model.vo.Certification;
import model.vo.User;

import java.io.IOException;

public class UserRecordsDetailsController{
    @FXML
    public Button button_refresh;

    @FXML
    public TableView certification_table;
    public Button certification_add_btn;
    public Button certification_delete_btn;
    public Button certification_edit_btn;
    public TableColumn certification_column;
    public TableColumn year_column;

    private int user_id;

    public void button_refresh_pressed(ActionEvent event) {
        refreshTable(certification_table);
    }


    public void initUserId(User user){
        user_id = user.getId();

        certification_column.setCellValueFactory(
                new PropertyValueFactory<Certification,String>("Qualification")
        );
        year_column.setCellValueFactory(
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
            JDBCCertificationDao jdbcCertificationDao= new JDBCCertificationDao();
            certification_table.setItems(jdbcCertificationDao.select(user_id));
        }

    }



}

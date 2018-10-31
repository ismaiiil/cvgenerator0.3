package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.dao.jdbc.JDBCUserDao;
import model.vo.User;

import java.io.IOException;
import java.net.URL;
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
        refreshTable();


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
        JDBCUserDao jdbcUserDao = new JDBCUserDao();
        if(user != null){
            jdbcUserDao.delete(user.getId());
        }
        refreshTable();
    }

    public void add_btn_pressed(ActionEvent event) {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/UserBasicDetails.fxml"));
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
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/UserBasicDetails.fxml"));
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
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/UserRecordsDetails.fxml"));
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
}
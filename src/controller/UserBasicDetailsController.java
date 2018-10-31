package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dao.jdbc.JDBCUserDao;
import model.vo.User;

public class UserBasicDetailsController {
    public Button cancel_btn;
    public Button save_btn;
    public Button next_btn;
    public TextField name_text;
    public TextField surname_text;
    public TextField email_text;
    public TextField position_text;

    int user_id;
    User user;
    boolean button_pressed;
    public void init_user_details(User user){
        this.user = user;
        user_id = user.getId();
    }

    public void initdata() {
        name_text.setText(user.getName());
        surname_text.setText(user.getSurname());
        email_text.setText(user.getEmail());
        position_text.setText(user.getPosition());
    }

    public void cancel_btn_pressed(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

    public void save_btn_pressed(ActionEvent event) {
        commitUser();
        button_pressed = true;
    }

    private void commitUser() {
        User user = new User();
        user.setName(name_text.getText());
        user.setSurname(surname_text.getText());
        user.setEmail(email_text.getText());
        user.setPosition(position_text.getText());
        JDBCUserDao jdbcUserDao = new JDBCUserDao();
        if(user_id==0){
            jdbcUserDao.insert(user);
            closeWindow();
        }else{
            user.setId(user_id);
            jdbcUserDao.update(user);
            closeWindow();
        }
    }

    public void next_btn_pressed(ActionEvent event) {
        commitUser();
        button_pressed = true;
    }



}

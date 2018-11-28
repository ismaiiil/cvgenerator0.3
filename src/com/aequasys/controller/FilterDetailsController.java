package com.aequasys.controller;

import com.aequasys.model.dao.jdbc.JDBCFilterDao;
import com.aequasys.model.vo.Filter;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javax.sql.rowset.JdbcRowSet;
import java.net.URL;
import java.util.ResourceBundle;

public class FilterDetailsController implements Initializable{
    public TableView masteries_table;
    public TableColumn masteries_column;
    public Button masteries_add_btn;
    public Button masteries_delete_btn;
    public TextField textfield_add;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        masteries_column.setCellValueFactory(
                new PropertyValueFactory<Filter,String>("mastery")
        );
        masteries_table.setEditable(true);
        masteries_column.setCellFactory(TextFieldTableCell.forTableColumn());
        refreshTable();

    }

    private void refreshTable() {
        JDBCFilterDao jdbcFilterDao = new JDBCFilterDao();
        masteries_table.setItems(jdbcFilterDao.select());
    }

    public void masteries_add_btn_pressed(ActionEvent event) {
        JDBCFilterDao jdbcFilterDao = new JDBCFilterDao();
        if(textfield_add.getText().trim().length() > 0){
            Filter filter = new Filter();
            filter.setMastery(textfield_add.getText());
            jdbcFilterDao.insert(filter);
            textfield_add.setText("");
            refreshTable();
        }

    }

    public void masteries_delete_btn_pressed(ActionEvent event) {
        Filter filter = (Filter) masteries_table.getSelectionModel().getSelectedItem();
        if(filter!=null){
            JDBCFilterDao jdbcFilterDao = new JDBCFilterDao();
            jdbcFilterDao.delete(filter.getId());
        }
        refreshTable();
    }

    public void on_edit_commit_masteries(TableColumn.CellEditEvent cellEditEvent) {
        Filter filter = (Filter) masteries_table.getSelectionModel().getSelectedItem();
        JDBCFilterDao jdbcFilterDao = new JDBCFilterDao();
        Filter editedFilter = new Filter();
        editedFilter.setId(filter.getId());
        editedFilter.setMastery(cellEditEvent.getNewValue().toString());
        jdbcFilterDao.update(editedFilter);
    }
}

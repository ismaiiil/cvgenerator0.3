package com.aequasys.controller;

import com.aequasys.eventsClasses.ErrorLogger;
import com.aequasys.eventsClasses.IntField;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.aequasys.model.dao.jdbc.JDBCReportDao;
import com.aequasys.model.vo.Report;

public class ReportDetailsController {
    public Button cancel_btn;
    public Button save_btn;
    public TextField country_text;
    public TextField report_year_text;
    public TextArea report_details_text;

    private Report passedReport;
    boolean button_pressed;

    public void init_report(Report report){
        passedReport = report;
        if(passedReport.getId()!=0){
            country_text.setText(passedReport.getCountry());
            report_year_text.setText(String.valueOf(passedReport.getYear()));
            report_details_text.setText(passedReport.getDetails());
        }
        IntField.convertToIntField(report_year_text);
    }

    private void commitReport(){
        if(!report_year_text.getText().equals("")){
            Report report = new Report();
            report.setUser_id(passedReport.getUser_id());
            report.setYear(Integer.parseInt(report_year_text.getText()));
            report.setCountry(country_text.getText());
            report.setDetails(report_details_text.getText());
            JDBCReportDao jdbcReportDao = new JDBCReportDao();
            if(passedReport.getId()==0){
                jdbcReportDao.insert(report);
            }else{
                report.setId(passedReport.getId());
                jdbcReportDao.update(report);
            }
            button_pressed=true;
            closeWindow();
        }

    }

    private void closeWindow() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

    public void cancel_btn_pressed(ActionEvent event) {
        closeWindow();
        button_pressed=true;
    }

    public void save_btn_pressed(ActionEvent event) {
        commitReport();
    }
}

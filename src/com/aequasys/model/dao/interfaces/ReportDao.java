package com.aequasys.model.dao.interfaces;

import javafx.collections.ObservableList;
import com.aequasys.model.vo.Report;

public interface ReportDao {
    public void insert(Report report);
    public ObservableList<Report> select(int id);
    public ObservableList<Report> selectByLastYears(int id,int yearDifference);
    public void delete(int id);
    public void update(Report report);
}

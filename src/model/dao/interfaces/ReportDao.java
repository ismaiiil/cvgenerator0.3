package model.dao.interfaces;

import javafx.collections.ObservableList;
import model.vo.Report;

public interface ReportDao {
    public void insert(Report report);
    public ObservableList<Report> select(int id);
    public void delete(int id);
    public void update(Report report);
}

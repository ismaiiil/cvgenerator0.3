package model.dao.jdbc;

import javafx.collections.ObservableList;
import model.dao.interfaces.ReportDao;
import model.vo.Report;

public class JDBCReportDao implements ReportDao {
    @Override
    public void insert(Report report) {

    }

    @Override
    public ObservableList<Report> select(int user_id) {
        return null;
    }

    @Override
    public void delete(int user_id) {

    }

    @Override
    public void update(Report report) {

    }
}

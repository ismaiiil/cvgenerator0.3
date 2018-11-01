package model.dao.jdbc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.connection.ConnectionFactory;
import model.dao.interfaces.ReportDao;
import model.vo.Report;

import java.sql.*;

public class JDBCReportDao implements ReportDao {
    @Override
    public void insert(Report report) {
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO TEST.REPORTS(ID, YEAR, COUNTRY,DETAILS,USER_ID) VALUES (NULL, ?, ?,?,?)"
            );
            preparedStatement.setInt(1,report.getYear());
            preparedStatement.setString(2, report.getCountry());
            preparedStatement.setString(3,report.getDetails());
            preparedStatement.setInt(4,report.getUser_id());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ObservableList<Report> select(int user_id) {
        ObservableList<Report> reports = FXCollections.observableArrayList();
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TEST.REPORTS WHERE USER_ID = ?");
            preparedStatement.setInt(1,user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Report report = null;

            while(resultSet.next()){
                report = new Report();
                report.setId(Integer.parseInt(resultSet.getString("id")));
                report.setYear(resultSet.getInt("year"));
                report.setCountry(resultSet.getString("country"));
                report.setDetails(resultSet.getString("details"));
                report.setUser_id(resultSet.getInt("user_id"));
                reports.add(report);
            }
            resultSet.close();
            statement.close();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return reports;
    }

    @Override
    public void delete(int id) {
        Connection connection = null;

        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM TEST.REPORTS WHERE ID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("report with id: " + id + " was successfully deleted from DB.");
        }
        catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if(connection !=null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Report report) {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE TEST.REPORTS SET YEAR=?,COUNTRY=?,DETAILS=?,USER_ID=?,ID=? WHERE ID=?");

            ps.setInt(1,report.getYear());
            ps.setString(2,report.getCountry());
            ps.setString(3,report.getDetails());
            ps.setInt(4,report.getUser_id());
            ps.setInt(5,report.getId());
            ps.setInt(6,report.getId());
            int rowsAffected = ps.executeUpdate();
            ps.close();
            System.out.println(rowsAffected + " Rows affected.");
            System.out.println("qualification with id " + report.getId());
        }
        catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if(connection !=null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

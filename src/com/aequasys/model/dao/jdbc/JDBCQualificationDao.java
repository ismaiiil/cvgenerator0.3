package com.aequasys.model.dao.jdbc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.aequasys.model.dao.connection.ConnectionFactory;
import com.aequasys.model.dao.interfaces.QualificationDao;
import com.aequasys.model.vo.Qualification;

import java.sql.*;

public class JDBCQualificationDao implements QualificationDao {
    @Override
    public void insert(Qualification qualification) {
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO TEST.QUALIFICATIONS(ID, YEAR, DIPLOMA,UNIVERSITY,USER_ID) VALUES (NULL, ?, ?,?,?)"
            );
            preparedStatement.setInt(1,qualification.getYear());
            preparedStatement.setString(2, qualification.getDiploma());
            preparedStatement.setString(3,qualification.getUniversity());
            preparedStatement.setInt(4,qualification.getUser_id());
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
    public ObservableList<Qualification> select(int user_id) {
        ObservableList<Qualification> qualifications = FXCollections.observableArrayList();
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TEST.QUALIFICATIONS WHERE USER_ID = ? ORDER BY YEAR ASC ");
            preparedStatement.setInt(1,user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Qualification qualification = null;

            while(resultSet.next()){
                qualification = new Qualification();
                qualification.setId(Integer.parseInt(resultSet.getString("id")));
                qualification.setYear(resultSet.getInt("year"));
                qualification.setDiploma(resultSet.getString("diploma"));
                qualification.setUniversity(resultSet.getString("university"));
                qualification.setUser_id(resultSet.getInt("user_id"));
                qualifications.add(qualification);
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
        return qualifications;
    }

    @Override
    public void delete(int id) {
        Connection connection = null;

        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM TEST.QUALIFICATIONS WHERE ID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("qualification with id: " + id + " was successfully deleted from DB.");
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
    public void update(Qualification qualification) {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE TEST.QUALIFICATIONS SET YEAR=?,DIPLOMA=?,UNIVERSITY=?,USER_ID=?,ID=? WHERE ID=?");

            ps.setInt(1,qualification.getYear());
            ps.setString(2,qualification.getDiploma());
            ps.setString(3,qualification.getUniversity());
            ps.setInt(4,qualification.getUser_id());
            ps.setInt(5,qualification.getId());
            ps.setInt(6,qualification.getId());
            int rowsAffected = ps.executeUpdate();
            ps.close();
            System.out.println(rowsAffected + " Rows affected.");
            System.out.println("qualification with id " + qualification.getId());
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

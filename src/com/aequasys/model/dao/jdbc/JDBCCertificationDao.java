package com.aequasys.model.dao.jdbc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.aequasys.model.dao.connection.ConnectionFactory;
import com.aequasys.model.dao.interfaces.CertificationDao;
import com.aequasys.model.vo.Certification;

import java.sql.*;

public class JDBCCertificationDao implements CertificationDao {
    @Override
    public void insert(Certification certification) {
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO TEST.CERTIFICATIONS(ID, YEAR, QUALIFICATION, USER_ID) VALUES (NULL, ?, ?,?)"
            );
            preparedStatement.setInt(1,certification.getYear());
            preparedStatement.setString(2, certification.getQualification());
            preparedStatement.setInt(3,certification.getUser_id());
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
    public ObservableList<Certification> select(int user_id) {
        ObservableList<Certification> certifications = FXCollections.observableArrayList();
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TEST.CERTIFICATIONS WHERE USER_ID = ? ORDER BY YEAR ASC");
            preparedStatement.setInt(1,user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Certification certification = null;

            while(resultSet.next()){
                certification = new Certification();
                certification.setId(resultSet.getInt("id"));
                certification.setYear(resultSet.getInt("year"));
                certification.setQualification(resultSet.getString("qualification"));
                certification.setUser_id(resultSet.getInt("user_id"));
                certifications.add(certification);
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
        return certifications;
    }

    @Override
    public void delete(int id) {
        Connection connection = null;

        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM TEST.CERTIFICATIONS WHERE ID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("Certification with id: " + id + " was sucesfully deleted from DB.");
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
    public void update(Certification certification) {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE TEST.CERTIFICATIONS SET YEAR=?,QUALIFICATION=?,USER_ID=?,ID=? WHERE ID=?");

            ps.setInt(1,certification.getYear());
            ps.setString(2,certification.getQualification());
            ps.setInt(3,certification.getUser_id());
            ps.setInt(4,certification.getId());
            ps.setInt(5,certification.getId());
            int rowsAffected = ps.executeUpdate();
            ps.close();
            System.out.println(rowsAffected + " Rows affected.");
            System.out.println("certification with id " + certification.getId());
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

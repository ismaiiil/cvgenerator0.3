package com.aequasys.model.dao.jdbc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.aequasys.model.dao.connection.ConnectionFactory;
import com.aequasys.model.dao.interfaces.MasteryDao;
import com.aequasys.model.vo.Mastery;

import java.sql.*;

public class JDBCMasteryDao implements MasteryDao {
    @Override
    public void insert(Mastery mastery) {
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO TEST.MASTERIES(ID, TECHNOLOGY, EXPERIENCE, USER_ID) VALUES (NULL, ?, ?,?)"
            );
            preparedStatement.setString(1,mastery.getTechnology());
            preparedStatement.setInt(2, mastery.getExperience());
            preparedStatement.setInt(3,mastery.getUser_id());
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
    public ObservableList<Mastery> select(int user_id) {
        ObservableList<Mastery> masteries = FXCollections.observableArrayList();
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TEST.MASTERIES WHERE USER_ID = ?");
            preparedStatement.setInt(1,user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Mastery mastery = null;

            while(resultSet.next()){
                mastery = new Mastery();
                mastery.setId(resultSet.getInt("id"));
                mastery.setTechnology(resultSet.getString("technology"));
                mastery.setExperience(resultSet.getInt("experience"));
                mastery.setUser_id(resultSet.getInt("user_id"));
                masteries.add(mastery);
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
        return masteries;
    }

    @Override
    public void delete(int id) {
        Connection connection = null;

        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM TEST.MASTERIES WHERE ID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("mastery with id: " + id + " was sucesfully deleted from DB.");
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
    public void update(Mastery mastery) {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE TEST.MASTERIES SET TECHNOLOGY=?,EXPERIENCE=?,USER_ID=?,ID=? WHERE ID=?");

            ps.setString(1,mastery.getTechnology());
            ps.setInt(2,mastery.getExperience());
            ps.setInt(3,mastery.getUser_id());
            ps.setInt(4,mastery.getId());
            ps.setInt(5,mastery.getId());
            int rowsAffected = ps.executeUpdate();
            ps.close();
            System.out.println(rowsAffected + " Rows updated.");
            System.out.println("mastery with id " + mastery.getId());
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

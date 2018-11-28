package com.aequasys.model.dao.jdbc;

import com.aequasys.model.dao.connection.ConnectionFactory;
import com.aequasys.model.dao.interfaces.FilterDao;
import com.aequasys.model.vo.Filter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class JDBCFilterDao implements FilterDao {
    @Override
    public void insert(Filter filter) {
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO TEST.FILTERS(ID, MASTERY) VALUES (NULL, ?)"
            );
            preparedStatement.setString(1,filter.getMastery());
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
    public ObservableList<Filter> select() {
        ObservableList<Filter> filters = FXCollections.observableArrayList();
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TEST.FILTERS");
            Filter filter = null;

            while(resultSet.next()){
                filter = new Filter();
                filter.setId(resultSet.getInt("id"));
                filter.setMastery(resultSet.getString("mastery"));
                filters.add(filter);
            }
            resultSet.close();
            statement.close();
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
        return filters;
    }

    @Override
    public void delete(int filter_id) {
        Connection connection = null;

        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM TEST.FILTERS WHERE ID=?");
            ps.setInt(1, filter_id);
            ps.executeUpdate();
            ps.close();
            System.out.println("filter with id: " + filter_id + " was sucesfully deleted from DB.");
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
    public void update(Filter filter) {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE TEST.FILTERS SET MASTERY=?,ID=? WHERE ID=?");

            ps.setString(1,filter.getMastery());
            ps.setInt(2,filter.getId());
            ps.setInt(3,filter.getId());
            int rowsAffected = ps.executeUpdate();
            ps.close();
            System.out.println(rowsAffected + " Rows affected.");
            System.out.println("filter with id " + filter.getId());
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

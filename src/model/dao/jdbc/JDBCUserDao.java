package model.dao.jdbc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.connection.ConnectionFactory;
import model.dao.interfaces.UserDao;
import model.vo.User;

import java.sql.*;

public class JDBCUserDao implements UserDao {

    @Override
    public void insert(User user){
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO TEST.USERS(ID, NAME, SURNAME,EMAIL,POSITION) VALUES (NULL, ?, ?,?,?)"
            );
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setString(4,user.getPosition());
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
    public ObservableList<User> select(){
        ObservableList<User> users = FXCollections.observableArrayList();
        Connection connection = null;
        try{
            connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TEST.USERS");
            User user = null;

            while(resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setEmail(resultSet.getString("email"));
                user.setPosition(resultSet.getString("position"));
                users.add(user);
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
        return users;
    }

    @Override
    public void delete(int user_id) {
        Connection connection = null;

        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM TEST.USERS WHERE ID=?");
            ps.setInt(1, user_id);
            ps.executeUpdate();
            ps.close();
            System.out.println("user with id: " + user_id + " was sucesfully deleted from DB.");
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
    public void update(User user) {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE TEST.USERS SET NAME=?,SURNAME=?,EMAIL=?,POSITION=?,ID=? WHERE ID=?");

            ps.setString(1,user.getName());
            ps.setString(2,user.getSurname());
            ps.setString(3,user.getEmail());
            ps.setString(4,user.getPosition());
            ps.setInt(5,user.getId());
            ps.setInt(6,user.getId());
            int rowsAffected = ps.executeUpdate();
            ps.close();
            System.out.println(rowsAffected + " Rows affected.");
            System.out.println("user with id " + user.getId());
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

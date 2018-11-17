package com.aequasys.model.dao.interfaces;

import javafx.collections.ObservableList;
import com.aequasys.model.vo.User;

public interface UserDao {
    public void insert(User user);
    public ObservableList<User> select();
    public void delete(int user_id);
    public void update(User user);
    public ObservableList<User> selectByNameSurname(String name,String surname);
}

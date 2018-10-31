package model.dao.interfaces;

import javafx.collections.ObservableList;
import model.vo.Mastery;

public interface MasteryDao {
    public void insert(Mastery mastery);
    public ObservableList<Mastery> select(int user_id);
    public void delete(int id);
    public void update(Mastery mastery);
}

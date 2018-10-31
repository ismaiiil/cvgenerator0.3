package model.dao.interfaces;

import javafx.collections.ObservableList;
import model.vo.Qualification;

public interface QualificationDao {
    public void insert(Qualification qualification);
    public ObservableList<Qualification> select(int user_id);
    public void delete(int id);
    public void update(Qualification qualification);
}

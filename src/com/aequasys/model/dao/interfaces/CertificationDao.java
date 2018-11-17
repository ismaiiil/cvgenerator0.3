package com.aequasys.model.dao.interfaces;

import javafx.collections.ObservableList;
import com.aequasys.model.vo.Certification;

public interface CertificationDao {
    public void insert(Certification certification);
    public ObservableList<Certification> select(int user_id);
    public void delete(int id);
    public void update(Certification certification);
}

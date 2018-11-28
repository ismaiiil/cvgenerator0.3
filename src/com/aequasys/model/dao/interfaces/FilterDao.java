package com.aequasys.model.dao.interfaces;

import com.aequasys.model.vo.Filter;
import javafx.collections.ObservableList;

public interface FilterDao {
    public void insert(Filter filter);
    public ObservableList<Filter> select();
    public void delete(int filter_id);
    public void update(Filter filter);
}

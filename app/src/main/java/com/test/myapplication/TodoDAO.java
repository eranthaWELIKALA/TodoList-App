package com.test.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDAO {
    @Insert
    long insert(Todo user);

    @Update
    void update(Todo user);

    @Delete
    void delete(Todo user);

    @Query("SELECT * FROM Todo ORDER BY date")
    LiveData<List<Todo>> getTodoList();
}

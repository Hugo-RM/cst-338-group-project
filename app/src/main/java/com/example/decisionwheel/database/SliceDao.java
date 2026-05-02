package com.example.decisionwheel.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.decisionwheel.wheel.Slice;

import java.util.List;

@Dao
public interface SliceDao {
    @Insert
    long insert(Slice slice);

    @Update
    void update(Slice slice);

    @Delete
    void delete(Slice slice);

    @Query("SELECT * FROM slice WHERE wheel_id = :wheelId")
    List<Slice> getSlicesForWheel(int wheelId);
}

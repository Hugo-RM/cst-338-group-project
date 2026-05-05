package com.example.decisionwheel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.decisionwheel.wheel.WheelEntity;

import java.util.List;

@Dao
public interface WheelDao {
    @Insert
    long insert(WheelEntity wheel);

    @Update
    void update(WheelEntity wheel);

    @Delete
    void delete(WheelEntity wheel);

    @Query("SELECT * FROM wheel WHERE user_id = :userId")
    List<WheelEntity> getWheelsForUser(int userId);

    @Query("SELECT * FROM wheel WHERE user_id = :userId")
    LiveData<List<WheelEntity>> getWheelsForUserLD(int userId);

    @Query("SELECT * FROM wheel WHERE id = :id LIMIT 1")
    WheelEntity findById(int id);

    @Transaction
    @Query("SELECT * FROM wheel WHERE id = :wheelId LIMIT 1")
    LiveData<WheelWithSlices> getWheelWithSlicesById(int wheelId);
}

package com.example.decisionwheel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.WheelEntity;

import java.util.List;

@Dao
public interface WheelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(WheelEntity wheel);

    @Update
    void update(WheelEntity wheel);

    @Delete
    void delete(WheelEntity wheel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSlices(List<Slice> slices);

    @Transaction
    @Query("SELECT * FROM wheel WHERE user_id = :userId")
    LiveData<List<WheelWithSlices>> getWheelsWithSlicesByUserId(int userId);

    @Transaction
    @Query("SELECT * FROM wheel WHERE id = :wheelId")
    LiveData<WheelWithSlices> getWheelWithSlicesById(int wheelId);

    @Query("SELECT * FROM wheel WHERE user_id = :userId")
    LiveData<List<WheelEntity>> getWheelsByUserId(int userId);

    @Query("SELECT * FROM wheel WHERE user_id = :userId")
    LiveData<List<WheelEntity>> getWheelsForUserLD(int userId);

    @Query("SELECT * FROM wheel WHERE user_id = :userId")
    List<WheelEntity> getWheelsForUser(int userId);

    @Query("SELECT * FROM wheel WHERE id = :wheelId")
    WheelEntity findById(int wheelId);

    @Query("SELECT * FROM slice WHERE wheel_id = :wheelId")
    List<Slice> getSlicesByWheelId(int wheelId);

    @Query("DELETE FROM slice WHERE wheel_id = :wheelId")
    void deleteSlicesByWheelId(int wheelId);

    @Transaction
    default void insertWheelWithSlices(WheelEntity wheel, List<Slice> slices) {
        long wheelId = insert(wheel);
        for (Slice slice : slices) {
            slice.setWheelId((int) wheelId);
        }
        insertSlices(slices);
    }
}

package com.example.decisionwheel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;

import java.util.List;

@Dao
public interface WheelDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWheel(Wheel wheel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSlices(List<Slice> slices);

    @Transaction
    @Query("SELECT * FROM wheel_table WHERE userId = :userId")
    LiveData<List<WheelWithSlices>> getWheelsWithSlicesByUserId(int userId);

    @Transaction
    @Query("SELECT * FROM wheel_table WHERE id = :wheelId")
    LiveData<WheelWithSlices> getWheelWithSlicesById(int wheelId);

    @Query("SELECT * FROM wheel_table WHERE userId = :userId")
    LiveData<List<Wheel>> getWheelsByUserId(int userId);

    @Query("SELECT * FROM slice_table WHERE wheelId = :wheelId")
    List<Slice> getSlicesByWheelId(int wheelId);

    @Delete
    void deleteWheel(Wheel wheel);

    @Query("DELETE FROM slice_table WHERE wheelId = :wheelId")
    void deleteSlicesByWheelId(int wheelId);

    @Transaction
    default void insertWheelWithSlices(Wheel wheel, List<Slice> slices) {
        long wheelId = insertWheel(wheel);
        for (Slice slice : slices) {
            slice.setWheelId((int) wheelId);
        }
        insertSlices(slices);
    }
}

package com.example.decisionwheel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


// remember its for wheels not gym log activity recorder. this will be for identifying users from table
@Dao
public interface userDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM user_table WHERE username = :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM user_table WHERE id = :userId")
    LiveData<User> getUserById(int userId);
}
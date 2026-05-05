package com.example.decisionwheel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Query("SELECT * FROM user_table WHERE username = :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM user_table WHERE username = :username")
    User getUserByUserNameSync(String username);

    @Query("SELECT * FROM user_table WHERE id = :userId")
    LiveData<User> getUserById(int userId);
    
    @Query("SELECT COUNT(*) FROM user_table")
    int getUserCount();

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();
}
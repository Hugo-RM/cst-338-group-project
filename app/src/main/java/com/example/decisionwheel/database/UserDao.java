package com.example.decisionwheel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Delete
    void delete(UserEntity user);

    @Query("SELECT * FROM user WHERE username = :username")
    UserEntity findByUsername(String username);

    @Query("SELECT * FROM user WHERE username = :username")
    LiveData<UserEntity> findByUsernameLD(String username);

    @Query("SELECT * FROM user WHERE username LIKE '%' || :search || '%' ORDER BY username ASC")
    LiveData<List<UserEntity>> searchUsersLD(String search);

    @Query("SELECT * FROM user WHERE id = :userId")
    UserEntity findById(int userId);

    @Query("SELECT * FROM user ORDER BY username ASC")
    LiveData<List<UserEntity>> getAllUsersLD();

    @Query("SELECT * FROM user")
    List<UserEntity> getAllUsers();

    @Query("SELECT COUNT(*) FROM user")
    int getUserCount();

    @Query("SELECT * FROM user WHERE id = :userId")
    LiveData<UserEntity> findByIdLD(int userId);

    // Alias methods to support existing calls in the project
    @Query("SELECT * FROM user WHERE username = :username")
    LiveData<UserEntity> getUserByUserName(String username);

    @Query("SELECT * FROM user WHERE username = :username")
    UserEntity getUserByUserNameSync(String username);

    @Query("SELECT * FROM user WHERE id = :userId")
    LiveData<UserEntity> getUserById(int userId);
}

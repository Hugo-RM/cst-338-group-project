package com.example.decisionwheel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.database.UserDao;
import com.example.decisionwheel.database.UserEntity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserRepository {
    private final UserDao userDao;
    private static UserRepository repository;

    private UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.userDao = db.userDao();
    }

    public static UserRepository getRepository(Application application) {
        if (repository != null) return repository;
        Future<UserRepository> future = AppDatabase.databaseWriteExecutor.submit(
                (Callable<UserRepository>) () -> new UserRepository(application)
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("UserRepository", "Error getting repository");
        }
        return null;
    }

    public LiveData<UserEntity> getUserByUserName(String username) {
        return userDao.findByUsernameLD(username);
    }

    public LiveData<UserEntity> getUserById(int userId) {
        return userDao.findByIdLD(userId);
    }
}

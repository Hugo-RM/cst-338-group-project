package com.example.decisionwheel.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserRepository {
    private final UserDAO userDao;
    private static UserRepository repository;

    private UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.userDao = db.userDAO();
    }

    public static UserRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<UserRepository> future = AppDatabase.databaseWriteExecutor.submit(
                new Callable<UserRepository>() {
                    @Override
                    public UserRepository call() throws Exception {
                        return new UserRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("UserRepository", "Error getting repository");
        }
        return null;
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDao.getUserByUserName(username);
    }

    public LiveData<User> getUserById(int userId) {
        return userDao.getUserById(userId);
    }
}
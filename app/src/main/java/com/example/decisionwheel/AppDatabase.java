package com.example.decisionwheel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "decision_wheel_db";
    public static final String USER_TABLE = "user_table";
    private static volatile AppDatabase INSTANCE;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultUsers)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultUsers = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();

                User testUser = new User("testuser1", "testuser1");
                dao.insert(testUser);

                User admin = new User("admin2", "admin2");
                admin.setAdmin(true);
                dao.insert(admin);
            });
        }
    };

    public abstract UserDAO userDAO();
}
package com.example.decisionwheel.database;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Wheel.class, Slice.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "decision_wheel_db";
    public static final String USER_TABLE = "user_table";
    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    Log.d("AppDatabase", "Database onOpen triggered");
                                    databaseWriteExecutor.execute(() -> {
                                        seedDatabase(INSTANCE);
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void seedDatabase(AppDatabase db) {
        if (db == null) {
            Log.e("AppDatabase", "seedDatabase: Database instance is null");
            return;
        }
        
        try {
            UserDAO userDao = db.userDAO();
            WheelDAO wheelDao = db.wheelDAO();

            // Check if testuser1 exists
            User testUser1 = userDao.getUserByUserNameSync("testuser1");
            if (testUser1 == null) {
                Log.d("AppDatabase", "Seeding testuser1...");
                User newUser = new User("testuser1", "testuser1");
                long testUserId = userDao.insert(newUser);
                
                // Add a sample wheel for testuser1
                Wheel sampleWheel = new Wheel("Dinner Choices", (int) testUserId);
                List<Slice> slices = new ArrayList<>();
                slices.add(new Slice("Pizza", "Dinner Choices", Color.RED, 0));
                slices.add(new Slice("Tacos", "Dinner Choices", Color.GREEN, 0));
                slices.add(new Slice("Sushi", "Dinner Choices", Color.BLUE, 0));
                slices.add(new Slice("Burgers", "Dinner Choices", Color.YELLOW, 0));
                wheelDao.insertWheelWithSlices(sampleWheel, slices);
                Log.d("AppDatabase", "testuser1 and sample wheel seeded.");
            }

            // Check if admin2 exists
            User admin2 = userDao.getUserByUserNameSync("admin2");
            if (admin2 == null) {
                Log.d("AppDatabase", "Seeding admin2...");
                User newAdmin = new User("admin2", "admin2");
                newAdmin.setAdmin(true);
                userDao.insert(newAdmin);
                Log.d("AppDatabase", "admin2 seeded.");
            }
            
            Log.d("AppDatabase", "Database seed check complete.");
        } catch (Exception e) {
            Log.e("AppDatabase", "Error seeding database", e);
        }
    }

    public abstract UserDAO userDAO();
    public abstract WheelDAO wheelDAO();
}

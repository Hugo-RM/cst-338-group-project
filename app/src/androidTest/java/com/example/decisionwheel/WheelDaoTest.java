package com.example.decisionwheel;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.database.UserDao;
import com.example.decisionwheel.database.UserEntity;
import com.example.decisionwheel.database.WheelDao;
import com.example.decisionwheel.wheel.WheelEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WheelDaoTest {
    private AppDatabase db;
    private WheelDao wheelDao;
    private int testUserId;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        wheelDao = db.wheelDao();

        UserDao userDao = db.userDao();
        testUserId = (int) userDao.insert(new UserEntity("testuser", "pass"));
    }

    @After
    public void teardown() {
        db.close();
    }

    @Test
    public void insertAndFindWheel() {
        WheelEntity wheel = new WheelEntity("Food Choices", testUserId);
        long id = wheelDao.insert(wheel);

        WheelEntity found = wheelDao.findById((int) id);
        assertNotNull(found);
        assertEquals("Food Choices", found.getName());
    }

    @Test
    public void getWheelsForUser_returnsAllWheels() {
        wheelDao.insert(new WheelEntity("Food Choices", testUserId));
        wheelDao.insert(new WheelEntity("Weekend Plans", testUserId));

        List<WheelEntity> wheels = wheelDao.getWheelsForUser(testUserId);
        assertEquals(2, wheels.size());
    }

    @Test
    public void getWheelsForUser_returnsEmpty_whenNoneExist() {
        List<WheelEntity> wheels = wheelDao.getWheelsForUser(testUserId);
        assertTrue(wheels.isEmpty());
    }

    @Test
    public void deleteWheel() {
        long id = wheelDao.insert(new WheelEntity("Temp Wheel", testUserId));
        WheelEntity inserted = wheelDao.findById((int) id);
        wheelDao.delete(inserted);

        assertNull(wheelDao.findById((int) id));
    }
}

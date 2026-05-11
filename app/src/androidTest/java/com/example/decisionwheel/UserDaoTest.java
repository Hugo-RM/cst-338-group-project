package com.example.decisionwheel;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.database.UserDao;
import com.example.decisionwheel.database.UserEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {
    private AppDatabase db;
    private UserDao userDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
    }

    @After
    public void teardown() {
        db.close();
    }

    @Test
    public void insertAndFindByUsername() {
        UserEntity user = new UserEntity("hugo", "pass123");
        userDao.insert(user);

        UserEntity found = userDao.findByUsername("hugo");
        assertNotNull(found);
        assertEquals("hugo", found.getUsername());
    }

    @Test
    public void insertReturnsGeneratedId() {
        UserEntity user = new UserEntity("athian", "pass456");
        long id = userDao.insert(user);
        assertTrue(id > 0);
    }

    @Test
    public void findByUsername_returnsNull_whenNotFound() {
        UserEntity found = userDao.findByUsername("nobody");
        assertNull(found);
    }

    @Test
    public void deleteUser() {
        UserEntity user = new UserEntity("brandon", "pass789");
        long id = userDao.insert(user);

        UserEntity inserted = userDao.findById((int) id);
        userDao.delete(inserted);

        assertNull(userDao.findById((int) id));
    }
}

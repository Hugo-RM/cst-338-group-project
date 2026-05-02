package com.example.decisionwheel;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.database.SliceDao;
import com.example.decisionwheel.database.UserDao;
import com.example.decisionwheel.database.UserEntity;
import com.example.decisionwheel.database.WheelDao;
import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.WheelEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SliceDaoTest {
    private AppDatabase db;
    private SliceDao sliceDao;
    private int testWheelId;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        sliceDao = db.sliceDao();

        UserDao userDao = db.userDao();
        int userId = (int) userDao.insert(new UserEntity("testuser", "pass"));

        WheelDao wheelDao = db.wheelDao();
        testWheelId = (int) wheelDao.insert(new WheelEntity("Test Wheel", userId));
    }

    @After
    public void teardown() {
        db.close();
    }

    @Test
    public void insertAndGetSlicesForWheel() {
        Slice slice = new Slice("Pizza", "Food", android.graphics.Color.RED);
        slice.setWheelId(testWheelId);
        sliceDao.insert(slice);

        List<Slice> slices = sliceDao.getSlicesForWheel(testWheelId);
        assertEquals(1, slices.size());
        assertEquals("Pizza", slices.get(0).getObjective());
    }

    @Test
    public void multipleSlicesForSameWheel() {
        Slice s1 = new Slice("Pizza", "Food", android.graphics.Color.RED);
        s1.setWheelId(testWheelId);
        Slice s2 = new Slice("Sushi", "Food", android.graphics.Color.BLUE);
        s2.setWheelId(testWheelId);

        sliceDao.insert(s1);
        sliceDao.insert(s2);

        List<Slice> slices = sliceDao.getSlicesForWheel(testWheelId);
        assertEquals(2, slices.size());
    }

    @Test
    public void deleteSlice() {
        Slice slice = new Slice("Tacos", "Food", android.graphics.Color.GREEN);
        slice.setWheelId(testWheelId);
        long id = sliceDao.insert(slice);

        List<Slice> before = sliceDao.getSlicesForWheel(testWheelId);
        assertEquals(1, before.size());

        Slice inserted = before.get(0);
        sliceDao.delete(inserted);

        List<Slice> after = sliceDao.getSlicesForWheel(testWheelId);
        assertTrue(after.isEmpty());
    }

    @Test
    public void getSlicesForWheel_returnsEmpty_whenNoneExist() {
        List<Slice> slices = sliceDao.getSlicesForWheel(testWheelId);
        assertTrue(slices.isEmpty());
    }
}

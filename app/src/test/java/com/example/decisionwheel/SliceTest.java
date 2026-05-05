package com.example.decisionwheel;

import com.example.decisionwheel.wheel.Slice;

import org.junit.Test;

import static org.junit.Assert.*;

public class SliceTest {

    @Test
    public void constructor_setsAllFields() {
        Slice slice = new Slice("Movie Night", "Fun", 0xFF0000);
        assertEquals("Movie Night", slice.getObjective());
        assertEquals("Fun", slice.getCategory());
        assertEquals(0xFF0000, slice.getColor());
    }

    @Test
    public void twoArgConstructor_defaultsCategoryToUnassigned() {
        Slice slice = new Slice("Pizza", 0x00FF00);
        assertEquals("UNASSIGNED", slice.getCategory());
    }

    @Test
    public void setObjective_updatesValue() {
        Slice slice = new Slice("Old", 0xFF0000);
        slice.setObjective("New");
        assertEquals("New", slice.getObjective());
    }

    @Test
    public void setWheelId_updatesValue() {
        Slice slice = new Slice("Option", 0xFF0000);
        slice.setWheelId(42);
        assertEquals(42, slice.getWheelId());
    }

    @Test
    public void setColor_updatesValue() {
        Slice slice = new Slice("Option", 0xFF0000);
        slice.setColor(0x0000FF);
        assertEquals(0x0000FF, slice.getColor());
    }
}

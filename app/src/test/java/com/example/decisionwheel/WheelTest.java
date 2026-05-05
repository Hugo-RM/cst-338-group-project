package com.example.decisionwheel;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;

import org.junit.Test;

import static org.junit.Assert.*;

public class WheelTest {

    @Test
    public void insertSlice_addsSliceToList() {
        Wheel wheel = new Wheel();
        wheel.insertSlice(new Slice("Pizza", 0xFF0000));
        assertEquals(1, wheel.getSlices().size());
        assertEquals("Pizza", wheel.getSlices().get(0).getObjective());
    }

    @Test
    public void insertSlice_throwsWhenFull() {
        Wheel wheel = new Wheel();
        for (int i = 0; i < Wheel.MAX_SLICES; i++) {
            wheel.insertSlice(new Slice("Option " + i, 0xFF0000));
        }
        assertThrows(IllegalStateException.class, () ->
                wheel.insertSlice(new Slice("One Too Many", 0xFF0000))
        );
    }

    @Test
    public void removeSlice_removesAtIndex() {
        Wheel wheel = new Wheel();
        wheel.insertSlice(new Slice("A", 0xFF0000));
        wheel.insertSlice(new Slice("B", 0x00FF00));
        wheel.removeSlice(0);
        assertEquals(1, wheel.getSlices().size());
        assertEquals("B", wheel.getSlices().get(0).getObjective());
    }

    @Test
    public void removeSlice_throwsOnInvalidIndex() {
        Wheel wheel = new Wheel();
        assertThrows(IndexOutOfBoundsException.class, () -> wheel.removeSlice(0));
    }

    @Test
    public void getSlices_returnsEmptyListByDefault() {
        Wheel wheel = new Wheel();
        assertNotNull(wheel.getSlices());
        assertTrue(wheel.getSlices().isEmpty());
    }

    @Test
    public void categoryConstructor_setsCategory() {
        Wheel wheel = new Wheel("Weekend Plans");
        assertEquals("Weekend Plans", wheel.getCategory());
    }
}

package com.example.decisionwheel.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;

import java.util.List;

public class WheelWithSlices {
    @Embedded
    public Wheel wheel;

    @Relation(
            parentColumn = "id",
            entityColumn = "wheelId"
    )
    public List<Slice> slices;
}

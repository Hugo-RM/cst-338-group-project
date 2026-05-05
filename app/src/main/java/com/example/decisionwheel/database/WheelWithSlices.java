package com.example.decisionwheel.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.WheelEntity;

import java.util.List;

public class WheelWithSlices {
    @Embedded
    public WheelEntity wheel;

    @Relation(
            parentColumn = "id",
            entityColumn = "wheel_id"
    )
    public List<Slice> slices;
}

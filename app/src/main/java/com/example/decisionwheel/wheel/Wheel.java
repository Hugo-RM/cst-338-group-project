package com.example.decisionwheel.wheel;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.ArrayList;

@Entity(tableName = "wheel_table")
public class Wheel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String category;
    private int userId; // Reference to the owner

    @Ignore
    private ArrayList<Slice> slices;

    public Wheel() {
        this.slices = new ArrayList<>();
        this.category = "UNASSIGNED";
    }

    public Wheel(String category, int userId) {
        this.slices = new ArrayList<>();
        this.category = category;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Slice> getSlices() {
        return slices;
    }

    public void setSlices(ArrayList<Slice> slices) {
        this.slices = slices;
    }

    @Ignore
    private static final int MAX_SLICES = 5;

    public void insertSlice(Slice slice) {
        if (slices.size() < MAX_SLICES) {
            slices.add(slice);
        } else {
            throw new IllegalStateException("Wheel is full");
        }
    }
}

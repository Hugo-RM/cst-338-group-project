package com.example.decisionwheel.wheel;

import java.util.ArrayList;

public class Wheel {
    public static final int MAX_SLICES = 5;

    private String category;
    private int userId;
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

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public ArrayList<Slice> getSlices() { return slices; }
    public void setSlices(ArrayList<Slice> slices) { this.slices = slices; }

    public void insertSlice(Slice slice) {
        if (slices.size() < MAX_SLICES) {
            slices.add(slice);
        } else {
            throw new IllegalStateException("Wheel is full");
        }
    }
}

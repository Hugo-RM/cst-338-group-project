package com.example.decisionwheel.wheel;

import android.util.Log;

import java.util.ArrayList;

public class Wheel {
    public static final int MAX_SLICES = 5;
    private ArrayList<Slice> slices;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private static Boolean status = false;

    public Wheel() {
        slices = new ArrayList<>();
        category = "UNASSIGNED";
    }
    public Wheel(String category) {
        slices = new ArrayList<>();
        this.category = category;
    }
    public void insertSlice(Slice slice) {
        if (slices.size() < MAX_SLICES) {
            slices.add(slice);
            Log.i("Wheel", "Slice inserted");
        } else {
            throw new IllegalStateException("Wheel is full");
        }
    }

    public void replaceSlice(Slice slice, int index){
        if (index >= 0 && index < slices.size()) {
            slices.set(index, slice);
        } else {
            throw new IndexOutOfBoundsException("Invalid index");
        }
    }

    public void removeSlice(int index){
        if (index >= 0 && index < slices.size()) {
            slices.remove(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid index");
        }
    }

    public ArrayList<Slice> getSlices() {
        return slices;
    }

    public void removeSliceBySlice(Slice slice){
        try{
            slices.remove(slice);
        } catch (Exception e){
            System.out.println("Slice not found");
        }
    }

    public void setStatus(Boolean status) {
        Wheel.status = status;
    }

}

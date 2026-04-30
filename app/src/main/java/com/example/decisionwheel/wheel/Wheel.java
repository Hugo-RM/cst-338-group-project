package com.example.decisionwheel.wheel;

import java.util.ArrayList;

public class Wheel {
    private static final int MAX_SLICES = 5;
    private ArrayList<Slice> slices;

    private static Boolean status = false;


    public Wheel() {
        slices = new ArrayList<>();
    }
    public void insertSlice(Slice slice) {
        if (slices.size() < MAX_SLICES) {
            slices.add(slice);
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

    public void createDrawableSlice(){
        if (slices.size() >= 2) {
            for(int i = 0; i < slices.size(); i++) {
                //TODO: create drawble slice in xml
            }
        }
        else{
            System.out.println("Not enough slices");
        }
    }


}

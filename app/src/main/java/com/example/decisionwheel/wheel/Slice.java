package com.example.decisionwheel.wheel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "slice_table")
public class Slice {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String objective;
    private String category;
    private int color;
    private int wheelId; // Foreign key to Wheel

    public Slice(String objective, String category, int color, int wheelId){
        this.objective = objective;
        this.category = category;
        this.color = color;
        this.wheelId = wheelId;
    }

    public int getColor() {
        return color;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getWheelId() {
        return wheelId;
    }

    public void setWheelId(int wheelId) {
        this.wheelId = wheelId;
    }
}

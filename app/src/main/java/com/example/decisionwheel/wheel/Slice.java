package com.example.decisionwheel.wheel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "slice",
    foreignKeys = @ForeignKey(
        entity = WheelEntity.class,
        parentColumns = "id",
        childColumns = "wheel_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class Slice {
    // Primary key for the slice, auto-generated
    @PrimaryKey(autoGenerate = true)
    private int id;
    // Foreign key referencing the associated wheel
    @ColumnInfo(name = "wheel_id", index = true)
    private int wheelId;
    // Text describing the slice's objective
    private String objective;
    private String category;
    private int color;

    public Slice(String objective, String category, int color){
        this.objective = objective;
        this.category = category;
        this.color = color;
    }
    // Secondary constructor used by Room when loading from the database, ignores the category and defaults it to "UNASSIGNED"
    @Ignore
    public Slice(String objective, int color){
        this.objective = objective;
        this.color = color;
        this.category = "UNASSIGNED";
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
// Setter for id, added to allow Room to set the auto-generated ID when a new slice is inserted into the database
    public void setId(int id) {
        this.id = id;
    }
// Getter for wheelId, added to allow retrieval of the associated wheel's ID when needed
    public int getWheelId() {
        return wheelId;
    }
// Setter for wheelId, added to allow setting the association between a slice and its wheel after creation
    public void setWheelId(int wheelId) {
        this.wheelId = wheelId;
    }
// Setter for color, added to allow updating the color of a slice after creation
    public void setColor(int color) {
        this.color = color;
    }
}

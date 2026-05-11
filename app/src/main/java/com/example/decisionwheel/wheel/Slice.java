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
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "wheel_id", index = true)
    private int wheelId;

    private String objective;
    private String category;
    private int color;

    public Slice(String objective, String category, int color, int wheelId) {
        this.objective = objective;
        this.category = category;
        this.color = color;
        this.wheelId = wheelId;
    }

    @Ignore
    public Slice(String objective, String category, int color) {
        this.objective = objective;
        this.category = category;
        this.color = color;
    }

    @Ignore
    public Slice(String objective, int color) {
        this.objective = objective;
        this.color = color;
        this.category = "UNASSIGNED";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public int getWheelId() { return wheelId; }
    public void setWheelId(int wheelId) { this.wheelId = wheelId; }
}

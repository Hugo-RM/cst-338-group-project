package com.example.decisionwheel.wheel;

public class Slice {
    private String objective;
    private String category;
    private int id;

    private int color;

    public Slice(String objective, String category, int color){
        this.objective = objective;
        this.category = category;
        this.color = color;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

package com.example.decisionwheel.wheel;

public class Slice {
    private String objective;
    private String category;
    private int id;

    public Slice(String objective, String category){
        this.objective = objective;
        this.category = category;
    }

    public Slice(String objective){
        this.objective = objective;
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
}

package org.fitness_tracker.model;
import java.util.ArrayList;

public class Workout {
    int id;
    private String name;
    private ArrayList<ExerciseDetail> exerciseDetails;

    public Workout(int id, String name) {
        this.id = id;
        this.name = name;
    }


    // Gettery i settery
    public void setName(String name) { this.name = name; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String toString() {
        return name;
    }

}

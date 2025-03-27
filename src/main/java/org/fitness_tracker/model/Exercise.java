package org.fitness_tracker.model;
public class Exercise {
    private int ExerciseID;
    private String name;
    private String type;

    public Exercise(int ExerciseID , String name, String type) {
        this.ExerciseID = ExerciseID;
        this.name = name;
        this.type = type;
    }

    // Gettery i settery

    public int getExerciseID() {
        return ExerciseID;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String toString() {
        return name;
    }
}


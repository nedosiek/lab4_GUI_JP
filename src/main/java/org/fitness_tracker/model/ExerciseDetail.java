package org.fitness_tracker.model;

public class ExerciseDetail {
    private int id;
    private Exercise exercise;
    private int sets;
    private String unit;
    private float value;

    public ExerciseDetail(int id, Exercise exercise, int sets, String unit, float value) {
        this.id = id;
        this.exercise = exercise;
        this.sets = sets;
        this.unit = unit;
        this.value = value;
    }

    // Gettery i settery
    public Exercise getExercise() { return exercise; }
    public int getId() {return id;}
    public int getSets() { return sets; }
    public void setSets(int sets) { this.sets = sets; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public float getValue() { return value; }
    public void setValue(float value) { this.value = value; }

    @Override
    public String toString() {
        return ("Name: " + exercise.getName() + ", Set: " + sets + ", Unit: " + unit + ", Value: " + value);
    }
}


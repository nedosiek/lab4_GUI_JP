package org.fitness_tracker.model;

import java.time.LocalDate;

public class Goals {
    int id;
    private Exercise exercise;
    private float goalValue;
    private LocalDate deadline;

    public Goals(int id, Exercise exercise, float goalValue, LocalDate deadline) {
        this.id = id;
        this.exercise = exercise;
        this.goalValue = goalValue;
        this.deadline = deadline;
    }


    // Gettery i settery
    public Exercise getExercise() { return exercise; }
    public void updateGoalValue(float newGoalValue) {
        this.goalValue = newGoalValue;
    }
    public void updateDeadline(LocalDate newDeadline) {
        this.deadline = newDeadline;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public float getGoalValue() {
        return goalValue;
    }

    public int getId() {
        return id;
    }
}


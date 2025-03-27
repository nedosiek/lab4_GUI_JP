package org.fitness_tracker.model;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

public class TrainingSession {
    int id;
    private LocalDate date;
    private Workout workout;
    private ArrayList<ExerciseDetail> sessionDetails;

    public TrainingSession(int id, LocalDate date, Workout workout) {
        this.id = id;
        this.date = date;
        this.workout = workout;
        this.sessionDetails = new ArrayList<>();
    }


    // Gettery i settery
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Workout getWorkout() { return workout; }
    public void setWorkout(Workout workout) { this.workout = workout; }
    public List<ExerciseDetail> getSessionDetails() { return sessionDetails; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


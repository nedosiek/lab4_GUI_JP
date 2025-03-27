package org.fitness_tracker.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.fitness_tracker.db.DatabaseManager;
import org.fitness_tracker.model.ExerciseDetail;
import org.fitness_tracker.model.Workout;

import java.util.ArrayList;

public class WorkoutController extends MenuController {
    @FXML
    ScrollPane workoutPane;
    @FXML
    private TextField workoutField;
    @FXML
    private ComboBox<Workout> chooseWorkout;
    @FXML
    private ComboBox<ExerciseDetail> chooseExercise;

    @FXML
    public void initialize() {
        loadWorkout();
        loadExerciseDetail();
    }

    private void deleteWorkout(int id) {
        DatabaseManager.deleteWorkout(id);
        DatabaseManager.deleteWorkoutinWE(id);
        loadWorkout();
    }

    @FXML
    private void addWorkout() {
        String name = workoutField.getText();
        if (name.isEmpty()) {
            showAlert("Błąd", "Nazwa ćwiczenia nie może być pusta.");
        }
        else{
            DatabaseManager.addWorkout(name);
            loadWorkout();
            workoutField.clear();
        }
    }
    @FXML
    private void addExerciseToWorkout() {
        Workout selectedWorkout = chooseWorkout.getSelectionModel().getSelectedItem();
        ExerciseDetail selectedExercise = chooseExercise.getSelectionModel().getSelectedItem();

        if (selectedWorkout == null) {
            showAlert("Błąd", "Wybierz workout z listy.");
            return;
        }

        if (selectedExercise == null) {
            showAlert("Błąd", "Wybierz ćwiczenie z listy.");
            return;
        }

        int workout_id = selectedWorkout.getId();
        int exercise_id = selectedExercise.getId();
        DatabaseManager.addExercisetoWorkout(workout_id, exercise_id);
        loadWorkout();
        chooseExercise.getSelectionModel().clearSelection();
        chooseWorkout.getSelectionModel().clearSelection();
    }

    private void loadWorkout() {
        chooseWorkout.getItems().clear();
        ArrayList<VBox> workoutBoxes = new ArrayList<>();
        ArrayList<Workout> workouts = DatabaseManager.getWorkout();
        chooseWorkout.getItems().addAll(workouts);

        for (Workout w : workouts) {
            VBox workoutBox = new VBox();

            Label workoutLabel = new Label("Workout: " + w.toString());
            workoutLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            Button workoutEditButton = new Button("Edit");
            workoutEditButton.setOnAction(e -> editWorkout(w));

            Button workoutDeleteButton = new Button("Delete");
            workoutDeleteButton.setOnAction(e -> deleteWorkout(w.getId()));

            workoutBox.getChildren().addAll(workoutLabel, workoutEditButton, workoutDeleteButton);

            ArrayList<ExerciseDetail> exerciseDetails = DatabaseManager.getExerciseDetailsForWorkout(w.getId());
            for (ExerciseDetail detail : exerciseDetails) {
                VBox exerciseBox = new VBox();

                Label exerciseLabel = new Label(detail.toString());
                exerciseLabel.setStyle("-fx-font-size: 16px;");

                Button exerciseDeleteButton = new Button("Delete exercise");
                exerciseDeleteButton.setOnAction(e -> deleteDetail(w.getId(),detail.getId()));

                exerciseBox.getChildren().addAll(exerciseLabel ,exerciseDeleteButton);
                workoutBox.getChildren().addAll(exerciseBox);
            }

            workoutBoxes.add(workoutBox);
        }

        VBox container = new VBox();
        container.getChildren().addAll(workoutBoxes);
        workoutPane.setContent(container);
    }

    private void deleteDetail(int w_id, int ex_id) {
        DatabaseManager.deleteExercisefromWorkout(w_id,ex_id);
        loadWorkout();
    }

    private void editWorkout(Workout w) {
        Dialog<Workout> dialog = new Dialog<>();
        dialog.setTitle("Edit Workout");

        ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(w.toString());
        VBox vbox = new VBox();
        vbox.getChildren().addAll(new Label("Name:"), nameField);

        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                String newName = nameField.getText().trim();
                if (newName.isEmpty()) {
                    showAlert("Błąd", "Nazwa treningu nie może być pusta.");
                    return null;
                }
                w.setName(newName);
                return w;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedWorkout -> {
            DatabaseManager.updateWorkout(updatedWorkout.getId(),updatedWorkout.toString());
            loadWorkout();
        });
    }

    private void loadExerciseDetail() {
        ArrayList<ExerciseDetail> exerciseDetails = DatabaseManager.getExerciseDetails();
        chooseExercise.getItems().addAll(exerciseDetails);
    }
}

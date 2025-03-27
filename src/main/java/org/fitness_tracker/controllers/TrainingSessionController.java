package org.fitness_tracker.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.fitness_tracker.db.DatabaseManager;
import org.fitness_tracker.model.TrainingSession;
import org.fitness_tracker.model.Workout;

import java.time.LocalDate;
import java.util.ArrayList;

public class TrainingSessionController extends MenuController {
    @FXML
    private ScrollPane sessionPane;
    @FXML
    ComboBox sessionWorkout;
    @FXML
    DatePicker sessionDateSelected;

    @FXML
    public void initialize() {
        loadTrainSession();
        loadWorkouts();
    }

    private void loadWorkouts() {
        ArrayList<Workout> workouts = DatabaseManager.getWorkout();
        sessionWorkout.getItems().addAll(workouts);
    }

    @FXML
    private void loadTrainSession() {
        ArrayList<VBox> sessionsBox = new ArrayList<>();
        ArrayList<TrainingSession> trainingSessions = DatabaseManager.getWorkoutSessions();

        for (TrainingSession session : trainingSessions) {
            VBox sessionBox = new VBox();
            Label sessionLabel = new Label("Date: "+ session.getDate()+"    Workout: "+session.getWorkout());
            sessionLabel.setStyle("-fx-font-size: 16px;");

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> editSession(session));

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteSession(session.getId()));

            sessionBox.getChildren().addAll(sessionLabel, editButton, deleteButton);
            sessionsBox.add(sessionBox);
        }

        VBox container = new VBox();
        container.getChildren().addAll(sessionsBox);
        sessionPane.setContent(container);
    }

    private void deleteSession(int id) {
        DatabaseManager.deleteWorkoutSessions(id);
        loadTrainSession();
    }

    private void editSession(TrainingSession session) {
        Dialog<TrainingSession> dialog = new Dialog<>();
        dialog.setTitle("Edit Training Session");

        ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        VBox vbox = new VBox();
        DatePicker datePicker = new DatePicker(session.getDate());
        ComboBox<Workout> workoutComboBox = new ComboBox<>();
        workoutComboBox.getItems().addAll(DatabaseManager.getWorkout());
        workoutComboBox.setValue(session.getWorkout());

        vbox.getChildren().addAll(new Label("Date:"), datePicker, new Label("Workout:"), workoutComboBox);
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                LocalDate newDate = datePicker.getValue();
                Workout newWorkout = workoutComboBox.getValue();
                if (newDate != null && newWorkout != null) {
                    session.setDate(newDate);
                    session.setWorkout(newWorkout);
                    return session;
                } else {
                    showAlert("Błąd", "Wszystkie pola muszą być wypełnione.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedSession -> {
            DatabaseManager.updateTrainingSession(updatedSession);
            loadTrainSession();
        });
    }
    @FXML
    private void addSession() {
        Workout selectedWorkout = (Workout) sessionWorkout.getValue();
        LocalDate sessionDate = sessionDateSelected.getValue();

        if (selectedWorkout == null) {
            showAlert("Błąd", "Wybierz workout z listy.");
            return;
        }

        if (sessionDate == null) {
            showAlert("Błąd", "Wprowadź poprawną datę.");
            return;
        }

        try {
            DatabaseManager.addWorkoutSession(selectedWorkout.getId(), sessionDate.toString());
            loadTrainSession();
            clearFields();
        } catch (Exception e) {
            showAlert("Błąd", "Wystąpił problem podczas dodawania sesji.");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        sessionWorkout.setValue(null);
        sessionDateSelected.setValue(null);
    }
}


package org.fitness_tracker.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.fitness_tracker.db.DatabaseManager;
import org.fitness_tracker.model.Exercise;
import org.fitness_tracker.model.Goals;


import java.time.LocalDate;
import java.util.ArrayList;

public class GoalController extends MenuController {
    @FXML
    private ScrollPane GoalPane;
    @FXML
    ComboBox exerciseGoalComboBox;

    @FXML
    private TextField valueGoalField;
    @FXML
    private DatePicker deadlinePicker;
    @FXML
    public void initialize() {
        loadGoal();
        loadExercisesIntoComboBox();
    }

    private void loadExercisesIntoComboBox() {
        ArrayList<Exercise> exercises = DatabaseManager.getExercises();
        exerciseGoalComboBox.getItems().addAll(exercises);
    }

    @FXML
    private void loadGoal() {
        ArrayList<VBox> goals = new ArrayList<>();
        ArrayList<Goals> goalList = DatabaseManager.getUserGoals();

        for (Goals goal : goalList) {
            if(goal.getDeadline().isBefore(LocalDate.now())) {
                DatabaseManager.deleteUserGoal(goal.getId());
                continue;
            }
            VBox goalBox = new VBox();
            Label goalLabel = new Label("Name: " + goal.getExercise().getName()+"   Goal: "+goal.getGoalValue()+"   Deadline: "+goal.getDeadline());
            goalLabel.setStyle("-fx-font-size: 16px;");

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> editGoal(goal));

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteGoal(goal.getId()));

            goalBox.getChildren().addAll(goalLabel, editButton, deleteButton);
            goals.add(goalBox);
        }

        VBox container = new VBox();
        container.getChildren().addAll(goals);
        GoalPane.setContent(container);
    }
    @FXML
    private void addGoal() {
        Exercise selectedExercise = (Exercise) exerciseGoalComboBox.getValue();
        String goalValue = valueGoalField.getText();
        LocalDate deadline = null;

        try {
            deadline = deadlinePicker.getValue();
            if (deadline == null) {
                throw new IllegalArgumentException("Niepoprawna data");
            }
        } catch (Exception e) {
            showAlert("Błąd", "Wprowadź poprawną datę.");
            return;
        }

        if (selectedExercise == null || goalValue.isEmpty()) {
            showAlert("Błąd", "Wszystkie pola muszą być wypełnione.");
            return;
        }

        try {
            float goal = Float.parseFloat(goalValue);
            DatabaseManager.addUserGoal(selectedExercise.getExerciseID(), goal, deadline.toString());
            loadGoal();
            clearGoalFields();
        } catch (NumberFormatException e) {
            showAlert("Błąd", "Wprowadź poprawną wartość liczbową dla celu.");
        }
    }
    @FXML
    private void deleteGoal(int id) {
        DatabaseManager.deleteUserGoal(id);
        loadGoal();
    }
    private void editGoal(Goals goal) {
        Dialog<Goals> dialog = new Dialog<>();
        dialog.setTitle("Edit Goal");

        ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        TextField goalValueField = new TextField(String.valueOf(goal.getGoalValue()));
        DatePicker deadlinePicker = new DatePicker(goal.getDeadline());

        VBox vbox = new VBox();
        vbox.getChildren().addAll(new Label("Goal Value:"), goalValueField, new Label("Deadline:"), deadlinePicker);
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                try {
                    float newGoalValue = Float.parseFloat(goalValueField.getText());
                    LocalDate newDeadline = deadlinePicker.getValue();

                    if (newDeadline == null || newDeadline.isBefore(LocalDate.now())) {
                        showAlert("Błąd", "Data musi być dzisiejsza lub późniejsza.");
                        return null;
                    }

                    goal.updateGoalValue(newGoalValue);
                    goal.updateDeadline(newDeadline);
                    return goal;
                } catch (NumberFormatException e) {
                    showAlert("Błąd", "Wprowadź poprawną wartość liczbową dla celu.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedGoal -> {
            DatabaseManager.updateUserGoal(updatedGoal.getId(), updatedGoal.getGoalValue(), updatedGoal.getDeadline().toString());
            loadGoal();
        });
    }
    private void clearGoalFields() {
        exerciseGoalComboBox.setValue(null);
        valueGoalField.clear();
        deadlinePicker.setValue(null);
    }
}

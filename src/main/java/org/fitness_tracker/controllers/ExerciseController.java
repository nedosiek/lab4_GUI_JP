package org.fitness_tracker.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.fitness_tracker.db.DatabaseManager;
import org.fitness_tracker.model.Exercise;

import java.util.ArrayList;

public class ExerciseController extends MenuController {

    public ScrollPane scrollExercisePane;
    @FXML
    private TextField exerciseNameField;
    @FXML
    private ComboBox exerciseTypeField;

    @FXML
    public void initialize() {
        exerciseTypeField.getItems().addAll("Strength", "Cardio", "Flexibility","Crossfit");
        loadExercises();
    }

    @FXML
    private void loadExercises() {
        ArrayList<VBox> exercises = new ArrayList<>();
        ArrayList<Exercise> exerciseList = DatabaseManager.getExercises();

        for (Exercise ex : exerciseList) {
            VBox exerciseBox = new VBox();
            Label exerciseLabel = new Label("Name: " + ex.getName()+"     Type: " + ex.getType());
            exerciseLabel.setStyle("-fx-font-size: 16px;");

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> editExercise(ex));

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteExercise(ex.getExerciseID()));

            exerciseBox.getChildren().addAll(exerciseLabel, editButton, deleteButton);
            exercises.add(exerciseBox);
        }

        VBox container = new VBox();
        container.getChildren().addAll(exercises);
        scrollExercisePane.setContent(container);
    }

    @FXML
    private void addExercise() {
        String name = exerciseNameField.getText();
        String type = (String) exerciseTypeField.getValue();

        if (name.isEmpty()) {
            showAlert("Błąd", "Nazwa ćwiczenia nie może być pusta.");
            return;
        }

        if (type == null || type.isEmpty()) {
            showAlert("Błąd", "Typ ćwiczenia musi być wybrany.");
            return;
        }

        if (!name.isEmpty() && !type.isEmpty()) {
            DatabaseManager.addExercise(name, type);
            loadExercises();
            exerciseNameField.clear();
        }
    }
    private void editExercise(Exercise ex) {
        Dialog<Exercise> dialog = new Dialog<>();
        dialog.setTitle("Edit Exercise");


        ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(ex.getName());
        ComboBox<String> typeField = new ComboBox<>();
        typeField.getItems().addAll("Strength", "Cardio", "Flexibility", "Crossfit");
        typeField.setValue(ex.getType());

        VBox vbox = new VBox();
        vbox.getChildren().addAll(new Label("Name:"), nameField, new Label("Type:"), typeField);
        dialog.getDialogPane().setContent(vbox);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                ex.setName(nameField.getText());
                ex.setType(typeField.getValue());
                return ex;
            }
            return null;
        });


        dialog.showAndWait().ifPresent(updatedExercise -> {
            DatabaseManager.updateExercise(updatedExercise);
            loadExercises();
        });
    }

    private void deleteExercise(int id) {
        DatabaseManager.deleteExercise(id);
        DatabaseManager.deleteExerciseDetailbyExercise(id);
        loadExercises(); // Odśwież listę ćwiczeń po usunięciu
    }
}
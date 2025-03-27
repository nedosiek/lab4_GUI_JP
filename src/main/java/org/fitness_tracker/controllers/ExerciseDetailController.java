package org.fitness_tracker.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.fitness_tracker.db.DatabaseManager;
import org.fitness_tracker.model.Exercise;
import org.fitness_tracker.model.ExerciseDetail;

import java.util.ArrayList;

public class ExerciseDetailController extends MenuController {

    public ScrollPane scrollExerciseDetailPane;
    @FXML
    ComboBox exerciseComboBox;

    @FXML
    private TextField setsField;
    @FXML
    private TextField valueField;
    @FXML
    private TextField unitField;

    @FXML
    public void initialize() {
        loadExercisesDetails();
        loadExercisesIntoComboBox();
    }

    private void loadExercisesIntoComboBox() {
        ArrayList<Exercise> exercises = DatabaseManager.getExercises();
        exerciseComboBox.getItems().addAll(exercises);
    }
    @FXML
    private void loadExercisesDetails() {
        ArrayList<VBox> exerciseDetail = new ArrayList<>();
        ArrayList<ExerciseDetail> exerciseList = DatabaseManager.getExerciseDetails();

        for (ExerciseDetail detail : exerciseList) {
            VBox exerciseDetailBox = new VBox();
            Label exerciseDetailLabel = new Label("Exercise: " + detail.getExercise().getName()+
                    "     Type: " + detail.getExercise().getType()+
                    "     Sets: " +detail.getSets()+
                    "     Value: "+ detail.getValue()+
                    "     Unit: "+ detail.getUnit());
            exerciseDetailLabel.setStyle("-fx-font-size: 16px;");

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> editExerciseDetails(detail));

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteExerciseDetails(detail.getId()));

            exerciseDetailBox.getChildren().addAll(exerciseDetailLabel, editButton, deleteButton);
            exerciseDetail.add(exerciseDetailBox);
        }

        VBox container = new VBox();
        container.getChildren().addAll(exerciseDetail);
        scrollExerciseDetailPane.setContent(container);
    }
    @FXML
    private void addExerciseDetails() {
        try {
            int sets = Integer.parseInt(setsField.getText());
            float value = Float.parseFloat(valueField.getText());
            String unit = unitField.getText();
            Exercise selectedExercise = (Exercise) exerciseComboBox.getValue();
            if (selectedExercise == null) {
                showAlert("Błąd", "Wybierz ćwiczenie z listy.");
                return;
            }

            if (unit.isEmpty()) {
                showAlert("Błąd", "Jednostka nie może być pusta.");
                return;
            }

            DatabaseManager.addExerciseDetail(selectedExercise.getExerciseID(), sets, value, unit);
            loadExercisesDetails();
            clearFields();
        }catch (NumberFormatException e)
        {
            showAlert("Błąd", "Wprowadź poprawne wartości liczbowe dla zestawów i wartości.");
        }

    }

    private void clearFields() {
        setsField.clear();
        valueField.clear();
        unitField.clear();
        exerciseComboBox.setValue(null);
    }
    private void editExerciseDetails(ExerciseDetail detail) {
        Dialog<ExerciseDetail> dialog = new Dialog<>();
        dialog.setTitle("Edit Exercise Detail");

        ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        TextField setsField = new TextField(String.valueOf(detail.getSets()));
        TextField valueField = new TextField(String.valueOf(detail.getValue()));
        TextField unitField = new TextField(detail.getUnit());

        VBox vbox = new VBox();
        vbox.getChildren().addAll(
                new Label("Sets:"), setsField,
                new Label("Value:"), valueField,
                new Label("Unit:"), unitField
        );
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                try {
                    int sets = Integer.parseInt(setsField.getText());
                    float value = Float.parseFloat(valueField.getText());
                    String unit = unitField.getText();

                    if (unit.isEmpty()) {
                        showAlert("Błąd", "Jednostka nie może być pusta.");
                        return null;
                    }

                    detail.setSets(sets);
                    detail.setValue(value);
                    detail.setUnit(unit);
                    return detail;
                } catch (NumberFormatException e) {
                    showAlert("Błąd", "Wprowadź poprawne wartości liczbowe dla zestawów i wartości.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedDetail -> {
            DatabaseManager.updateExerciseDetail(updatedDetail.getId(), updatedDetail.getSets(), updatedDetail.getValue(), updatedDetail.getUnit());
            loadExercisesDetails();
        });
    }

    private void deleteExerciseDetails(int exerciseDetailID) {
        DatabaseManager.deleteExerciseDetail(exerciseDetailID);
        DatabaseManager.deleteExerciseDetailInWE(exerciseDetailID);
        loadExercisesDetails();
    }

}

package org.fitness_tracker.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import org.fitness_tracker.db.DatabaseManager;
import org.fitness_tracker.model.Exercise;
import org.fitness_tracker.model.TrainingSession;

import java.util.ArrayList;

public class VisualizationController extends MenuController {
    public ScrollPane visualizationPanel;
    @FXML
    private ComboBox<Exercise> chooseExerciseVisualization;


    @FXML
    public void initialize() {
        loadComboBoxExercise();
    }

    private void loadComboBoxExercise() {
        ArrayList<Exercise> exercises = DatabaseManager.getExercises();
        chooseExerciseVisualization.getItems().addAll(exercises);
    }

    @FXML
    private void loadChart()
    {
        visualizationPanel.setContent(null);
        Exercise selectedExercise = chooseExerciseVisualization.getValue();
        if (selectedExercise == null) {
            showAlert("Błąd", "Wybierz ćwiczenie z listy.");
            return;
        }
        ArrayList<TrainingSession> trainingSessions = DatabaseManager.getTrainingSessionsForExercise(selectedExercise.getExerciseID());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(selectedExercise.getName());

        for ( TrainingSession training : trainingSessions) {
            series.getData().add(new XYChart.Data<>(training.getDate().toString(), DatabaseManager.getValuesForExerciseOnDate(selectedExercise.getExerciseID(),training.getDate())));
        }

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Data");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wartość");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getData().add(series);
        barChart.setTitle("Wykres wartości dla " + selectedExercise.getName());

        visualizationPanel.setContent(barChart);
    }


}

package org.fitness_tracker.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuController {
    Stage stage;
    Scene scene;

    @FXML
    public void menuPageOnClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/fitness_tracker/mainView.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void exercisePageOnClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/fitness_tracker/exerciseView.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void workoutPageOnClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/fitness_tracker/workoutView.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void goalPageOnClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/fitness_tracker/goalView.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void trainingSessionPageOnClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/fitness_tracker/trainingSessionView.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void userViewPageOnClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/fitness_tracker/userView.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void VisualizationPageOnClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/fitness_tracker/visualizationView.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void exerciseDetailPageOnClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/fitness_tracker/exerciseDetailView.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

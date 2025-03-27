package org.fitness_tracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static org.fitness_tracker.db.DatabaseManager.closeConnection;
import static org.fitness_tracker.db.DatabaseManager.initializeDatabase;

public class FitnessTracker extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        initializeDatabase();
        FXMLLoader fxmlLoader = new FXMLLoader(FitnessTracker.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 964, 537);
        stage.setResizable(false);
        stage.setTitle("Fitness Tracker");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() {
        closeConnection();
    }
    public static void main(String[] args) {
        launch();
    }
}
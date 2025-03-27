module org.fitness_tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jdk.management;
    requires java.sql;

    opens org.fitness_tracker to javafx.fxml;
    exports org.fitness_tracker;
    exports org.fitness_tracker.controllers;
    opens org.fitness_tracker.controllers to javafx.fxml;
}
package com.spendwise;

import com.spendwise.domain.*;
import com.spendwise.repository.InterfaceRepository;
import com.spendwise.repository.SQLiteYearRepository;
import com.spendwise.service.YearService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));

        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();

        InterfaceRepository<Year> repoYear = new SQLiteYearRepository();
        YearService servYear = new YearService(repoYear);

        if (servYear.getAll().isEmpty()) {
            servYear.create(2024, true);
            servYear.create(2025, true);
            servYear.create(2026, false);
        }

        mainController.init(servYear);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("SpendWise!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

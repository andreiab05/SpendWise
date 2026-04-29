package com.spendwise;

import com.spendwise.domain.MonthlyBudgetEntry;
import com.spendwise.repository.InterfaceRepository;
import com.spendwise.repository.SQLiteMonthlyBudgetEntryRepository;
import com.spendwise.service.MonthlyBudgetEntryService;
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

        InterfaceRepository<MonthlyBudgetEntry> repoEntries = new SQLiteMonthlyBudgetEntryRepository();
        MonthlyBudgetEntryService servEntries = new MonthlyBudgetEntryService(repoEntries);

        if (servEntries.getAll().isEmpty()) {
            servEntries.create(2024, 10, "Meow", 50f, 100f);
            servEntries.create(2025, 4, "Meow", 25f, 100f);
            servEntries.create(2026, 12, "Meow", 75f, 100f);
        }

        mainController.init(servEntries);

        Scene scene = new Scene(root, 405, 600);
        stage.setTitle("SpendWise!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

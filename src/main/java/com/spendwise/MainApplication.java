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

        mainController.init(servEntries);

        Scene scene = new Scene(root, 405, 600);
        stage.setTitle("SpendWise!");
        stage.setScene(scene);
        stage.show();
    }

    //TODO Drag and drop categories
    //TODO Color of moneySpent green ? red depending if under/overflow
    //TODO Tidy up buttons/text fields
    //TODO Able to rename category or change budget
    //TODO Able to add a category for all months
    //TODO Make years before current one read only

    public static void main(String[] args) {
        launch(args);
    }
}

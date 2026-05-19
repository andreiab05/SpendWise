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

        Scene scene = new Scene(root, 460, 615);
        stage.setTitle("SpendWise!");
        stage.setScene(scene);
        stage.show();
    }

    //TODO NumberFormatException for create method
    //TODO Amounts must be bounded ( != 0 ; < 0.01f ; >9999999 )
    //TODO Make category case insensitive

    //TODO Make some sort of graphs, like a pie chart for example
    //TODO Make the GUI look better aesthetically
    //TODO Report per months
    //TODO Export as CSV

}

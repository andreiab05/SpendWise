package com.spendwise;

import com.spendwise.domain.MonthlyBudgetEntry;
import com.spendwise.service.MonthlyBudgetEntryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class MainController {
    @FXML TableView<MonthlyBudgetEntry> monthlyBudgetTableView;
    private ObservableList<MonthlyBudgetEntry> entries;
    @FXML private ComboBox<MonthlyBudgetEntry> entryComboBox;
    @FXML private Label selectedEntryLabel;
    private MonthlyBudgetEntryService monthlyBudgetEntryService;

    public void init(MonthlyBudgetEntryService entriesService) {
        this.monthlyBudgetEntryService = entriesService;

        entryComboBox.getItems().setAll(entriesService.getAll());

        entries = FXCollections.observableArrayList(entriesService.getAll());
        monthlyBudgetTableView.setItems(entries);

        entryComboBox.setOnAction(e -> {
            MonthlyBudgetEntry selectedEntry = entryComboBox.getValue();
            if (selectedEntry != null) {
                selectedEntryLabel.setText(
                        "You have selected the entry: " + selectedEntry.toString()
                );
            }
        });
    }
}

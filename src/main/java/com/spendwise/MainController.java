package com.spendwise;

import com.spendwise.domain.MonthlyBudgetEntry;
import com.spendwise.service.MonthlyBudgetEntryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {
    @FXML TableView<MonthlyBudgetEntry> monthlyBudgetTableView;
    @FXML TableColumn<MonthlyBudgetEntry, String> categoryCol;
    @FXML TableColumn<MonthlyBudgetEntry, Float> spentCol;
    @FXML TableColumn<MonthlyBudgetEntry, Float> budgetCol;
    private ObservableList<MonthlyBudgetEntry> entries;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private ComboBox<Integer> monthComboBox;
    @FXML private Label selectedEntryLabel;
    private MonthlyBudgetEntryService monthlyBudgetEntryService;

    public void init(MonthlyBudgetEntryService entriesService) {
        this.monthlyBudgetEntryService = entriesService;

        entries = FXCollections.observableArrayList(entriesService.getAll());
        monthlyBudgetTableView.setItems(entries);

        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        spentCol.setCellValueFactory(new PropertyValueFactory<>("moneySpent"));
        budgetCol.setCellValueFactory(new PropertyValueFactory<>("monthlyBudget"));

        yearComboBox.setOnAction(e -> refreshTable());
        monthComboBox.setOnAction(e -> refreshTable());
    }

    private void refreshTable() {
        Integer selectedYear = yearComboBox.getValue();
        Integer selectedMonth = monthComboBox.getValue();

        if (selectedYear == null || selectedMonth == null) {
            return;
        }

        entries.setAll(
                monthlyBudgetEntryService.getEntriesForMonth(selectedYear, selectedMonth)
        );
    }
}

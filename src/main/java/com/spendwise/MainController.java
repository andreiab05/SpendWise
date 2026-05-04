package com.spendwise;

import com.spendwise.domain.MonthlyBudgetEntry;
import com.spendwise.service.MonthlyBudgetEntryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Duration;

import static java.lang.Integer.parseInt;

public class MainController {
    @FXML TableView<MonthlyBudgetEntry> monthlyBudgetTableView;
    @FXML TableColumn<MonthlyBudgetEntry, String> categoryCol;
    @FXML TableColumn<MonthlyBudgetEntry, Float> spentCol;
    @FXML TableColumn<MonthlyBudgetEntry, Float> budgetCol;
    private ObservableList<MonthlyBudgetEntry> entries;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private ComboBox<Integer> monthComboBox;
    private MonthlyBudgetEntryService monthlyBudgetEntryService;

    @FXML public TextField textCategoryName;
    @FXML public TextField textMoneySpent;
    @FXML public TextField textMonthlyBudget;

    public void init(MonthlyBudgetEntryService entriesService) {
        this.monthlyBudgetEntryService = entriesService;

        entries = FXCollections.observableArrayList(entriesService.getAll());
        monthlyBudgetTableView.setItems(entries);

        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        spentCol.setCellValueFactory(new PropertyValueFactory<>("moneySpent"));
        budgetCol.setCellValueFactory(new PropertyValueFactory<>("monthlyBudget"));

        yearComboBox.getItems().setAll(2024, 2025, 2026);
        monthComboBox.getItems().setAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        yearComboBox.setOnAction(e -> refreshTable());
        monthComboBox.setOnAction(e -> refreshTable());

        yearComboBox.setValue(2026);
        monthComboBox.setValue(1);

        refreshTable();
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

    @FXML
    public void onAddClicked(ActionEvent actionEvent) {
        try {
            Integer selectedYear = yearComboBox.getValue();
            Integer selectedMonth = monthComboBox.getValue();

            if (selectedYear == null || selectedMonth == null) {
                throw new IllegalArgumentException("Please select year and month.");
            }

            String categoryName = textCategoryName.getText();
            float moneySpent = parseInt(textMoneySpent.getText());
            float monthlyBudget = parseInt(textMonthlyBudget.getText());

            monthlyBudgetEntryService.create(
                    selectedYear,
                    selectedMonth,
                    categoryName,
                    moneySpent,
                    monthlyBudget
            );

            refreshTable();

            textCategoryName.clear();
            textMoneySpent.clear();
            textMonthlyBudget.clear();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}

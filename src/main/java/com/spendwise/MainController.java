package com.spendwise;

import com.spendwise.domain.MonthlyBudgetEntry;
import com.spendwise.service.MonthlyBudgetEntryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
        spentCol.setCellFactory(column -> new TableCell<MonthlyBudgetEntry, Float>() {
            @Override
            protected void updateItem(Float moneySpent, boolean empty) {
                super.updateItem(moneySpent, empty);

                if (empty || moneySpent == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(String.valueOf(moneySpent));

                int index = getIndex();

                if (index < 0 || index >= getTableView().getItems().size()) {
                    setStyle("");
                    return;
                }

                MonthlyBudgetEntry entry = getTableView().getItems().get(index);

                if (entry.getMoneySpent() > entry.getMonthlyBudget()) {
                    setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                } else {
                    setStyle("");
                }
            }
        });
        budgetCol.setCellValueFactory(new PropertyValueFactory<>("monthlyBudget"));

        int currentYear = java.time.Year.now().getValue();
        yearComboBox.getItems().setAll(
                currentYear - 2,
                currentYear - 1,
                currentYear
        );
        monthComboBox.getItems().setAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        yearComboBox.setOnAction(e -> refreshTable());
        monthComboBox.setOnAction(e -> refreshTable());

        yearComboBox.setValue(java.time.Year.now().getValue());
        monthComboBox.setValue(java.time.LocalDate.now().getMonthValue());

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

            String categoryName = textCategoryName.getText().trim();

            if (categoryName.isBlank()) {
                throw new IllegalArgumentException("Category name cannot be empty.");
            }

            float moneySpent = Float.parseFloat(textMoneySpent.getText().trim());
            float monthlyBudget = Float.parseFloat(textMonthlyBudget.getText().trim());

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

    @FXML
    public void onAddToAllMonths(ActionEvent actionEvent) {
        try {
            Integer selectedYear = yearComboBox.getValue();

            if (selectedYear == null) {
                throw new IllegalArgumentException("Please select year.");
            }

            String categoryName = textCategoryName.getText().trim();

            if (categoryName.isBlank()) {
                throw new IllegalArgumentException("Category name cannot be empty.");
            }

            float monthlyBudget = Float.parseFloat(textMonthlyBudget.getText().trim());

            monthlyBudgetEntryService.addCategoryToAllMonths(
                    selectedYear,
                    categoryName,
                    monthlyBudget
            );

            refreshTable();

            textCategoryName.clear();
            textMonthlyBudget.clear();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void onDeleteClicked(ActionEvent actionEvent) {
        MonthlyBudgetEntry selected = monthlyBudgetTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select an entry first.", ButtonType.OK).showAndWait();
            return;
        }

        try {
            monthlyBudgetEntryService.delete(selected.getId());
            refreshTable();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void onUpdateEntryClicked(ActionEvent actionEvent) {
        MonthlyBudgetEntry selected = monthlyBudgetTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select an entry first.", ButtonType.OK).showAndWait();
            return;
        }

        try {
            if (textCategoryName.getText().trim().isBlank()
                    && textMoneySpent.getText().trim().isBlank()
                    && textMonthlyBudget.getText().trim().isBlank()) {
                throw new IllegalArgumentException("Please fill at least one field to update.");
            }

            String newCategoryName;
            float newMoneySpent;
            float newMonthlyBudget;

            if(textCategoryName.getText().trim().isBlank()){
                newCategoryName = selected.getCategoryName();
            } else {
                newCategoryName = textCategoryName.getText().trim();
            }

            if(textMoneySpent.getText().trim().isBlank()){
                newMoneySpent = selected.getMoneySpent();
            } else {
                newMoneySpent = Float.parseFloat(textMoneySpent.getText().trim());
            }

            if(textMonthlyBudget.getText().trim().isBlank()){
                newMonthlyBudget = selected.getMonthlyBudget();
            } else {
                newMonthlyBudget = Float.parseFloat(textMonthlyBudget.getText().trim());
            }

            monthlyBudgetEntryService.update(selected.getId(),
                    newCategoryName,
                    newMoneySpent,
                    newMonthlyBudget);

            refreshTable();
            textCategoryName.clear();
            textMoneySpent.clear();
            textMonthlyBudget.clear();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

}

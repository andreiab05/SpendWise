package com.spendwise;

import com.spendwise.domain.MonthlyBudgetEntry;
import com.spendwise.service.MonthlyBudgetEntryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

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

    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button updateButton;
    @FXML private Button addToAllMonthsButton;

    public void init(MonthlyBudgetEntryService entriesService) {
        this.monthlyBudgetEntryService = entriesService;

        entries = FXCollections.observableArrayList(entriesService.getAll());
        monthlyBudgetTableView.setItems(entries);

        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        spentCol.setCellValueFactory(new PropertyValueFactory<>("moneySpent"));
        spentCol.setCellFactory(_ -> new TableCell<>() {
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

        monthlyBudgetTableView.getSelectionModel().selectedItemProperty().addListener(
                (_, _, selectedEntry) -> {
                    if (selectedEntry != null) {
                        textCategoryName.setText(selectedEntry.getCategoryName());
                        textMoneySpent.setText(String.valueOf(selectedEntry.getMoneySpent()));
                        textMonthlyBudget.setText(String.valueOf(selectedEntry.getMonthlyBudget()));
                    }
                }
        );

        yearComboBox.setOnAction(e -> {
            refreshTable();
            updateButtonsForSelectedYear();
        });
        monthComboBox.setOnAction(e -> refreshTable());

        yearComboBox.setValue(java.time.Year.now().getValue());
        monthComboBox.setValue(java.time.LocalDate.now().getMonthValue());

        refreshTable();
        updateButtonsForSelectedYear();
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

    private void updateButtonsForSelectedYear() {
        Integer selectedYear = yearComboBox.getValue();
        int currentYear = java.time.Year.now().getValue();

        boolean isPastYear = selectedYear == null || selectedYear < currentYear;

        addButton.setDisable(isPastYear);
        deleteButton.setDisable(isPastYear);
        updateButton.setDisable(isPastYear);
        addToAllMonthsButton.setDisable(isPastYear);

        textCategoryName.setDisable(isPastYear);
        textMoneySpent.setDisable(isPastYear);
        textMonthlyBudget.setDisable(isPastYear);
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

            Float monthlyBudget = Float.parseFloat(textMonthlyBudget.getText().trim());

            String spentText = textMoneySpent.getText();
            Float moneySpent;

            if (spentText == null || spentText.isBlank()) {
                moneySpent = 0f;
            } else {
                moneySpent = Float.parseFloat(spentText);
            }

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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("This action cannot be undone");
            alert.setContentText("Do you want to continue?");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.isEmpty() || option.get() != ButtonType.OK) {
                return;
            }

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

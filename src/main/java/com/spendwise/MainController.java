package com.spendwise;

import com.spendwise.domain.Year;
import com.spendwise.service.YearService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class MainController {
    @FXML private ComboBox<Year> yearComboBox;
    @FXML private Label selectedYearLabel;
    private YearService yearService;

    public void init(YearService yearService) {
        this.yearService = yearService;

        yearComboBox.getItems().setAll(yearService.getAll());

        yearComboBox.setOnAction(e -> {
            Year selectedYear = yearComboBox.getValue();
            if (selectedYear != null) {
                selectedYearLabel.setText(
                        "You have selected the year: " + selectedYear.getYear()
                );
            }
        });
    }
}

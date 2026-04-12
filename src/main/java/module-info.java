module org.example.spendwise {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.spendwise to javafx.fxml;
    exports com.spendwise;
}
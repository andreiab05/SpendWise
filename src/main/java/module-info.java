module org.example.spendwise {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.spendwise to javafx.fxml;
    exports org.example.spendwise;
}
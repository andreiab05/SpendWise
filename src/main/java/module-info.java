module org.example.spendwise {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;


    opens com.spendwise to javafx.fxml;
    exports com.spendwise;
}
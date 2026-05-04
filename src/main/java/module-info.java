module org.example.spendwise {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;
    requires javafx.base;


    opens com.spendwise to javafx.fxml;
    opens com.spendwise.domain to javafx.base;
    exports com.spendwise;
}
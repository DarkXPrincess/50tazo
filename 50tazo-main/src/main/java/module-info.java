module com.example.minip3poe {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.minip3poe to javafx.fxml;
    opens com.example.minip3poe.controller to javafx.fxml;
    exports com.example.minip3poe;
}
module org.example.lab_4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.csv;
    requires io;
    requires kernel;
    requires layout;
    requires slf4j.log4j12;

    opens org.example.lab_4 to javafx.fxml;
    exports org.example.lab_4;
    exports org.example.lab_4.Controllers;
    opens org.example.lab_4.Controllers to javafx.fxml;
    opens org.example.lab_4.Models to javafx.base;
    opens org.example.lab_4.Models.Tables to javafx.base;
}
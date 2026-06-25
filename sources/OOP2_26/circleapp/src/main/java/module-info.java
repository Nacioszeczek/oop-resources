module pl.umcs.oop.circleapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens pl.umcs.oop.circleapp to javafx.fxml;
    exports pl.umcs.oop.circleapp;
}
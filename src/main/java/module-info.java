module be.intecbrussel {
    requires javafx.controls;
    requires javafx.fxml;
    exports be.intecbrussel;
    opens be.intecbrussel to javafx.fxml;
}
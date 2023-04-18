module com.example.banks {
    requires javafx.controls;
    requires javafx.fxml;
    requires jBCrypt;
    requires fx.onscreen.keyboard;


    opens com.example.banks to javafx.fxml;
    exports com.example.banks;
}
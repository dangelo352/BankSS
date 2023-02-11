module com.example.banks {
    requires javafx.controls;
    requires javafx.fxml;
    requires jBCrypt;


    opens com.example.banks to javafx.fxml;
    exports com.example.banks;
}
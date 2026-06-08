module com.vinyl.assignment1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.vinyl.assignment1 to javafx.fxml;
    exports com.vinyl.assignment1;
    
    opens com.vinyl.assignment1.view to javafx.fxml;
    exports com.vinyl.assignment1.view;

    opens com.vinyl.assignment1.model to com.google.gson;
    exports com.vinyl.assignment1.model;

    opens com.vinyl.assignment1.network to com.google.gson;
    exports com.vinyl.assignment1.network;
}

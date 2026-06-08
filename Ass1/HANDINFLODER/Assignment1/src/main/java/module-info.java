module com.vinyl.assignment1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vinyl.assignment1 to javafx.fxml;
    exports com.vinyl.assignment1;
    
    opens com.vinyl.assignment1.view to javafx.fxml;
    exports com.vinyl.assignment1.view;
}

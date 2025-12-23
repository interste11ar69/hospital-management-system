module marc.tiredatthehospital {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    exports marc;
    exports marc.DAO;
    exports marc.Connection;
    exports marc.View;
    exports marc.Class;

    opens marc to javafx.fxml;
    opens marc.DAO to javafx.fxml;
    opens marc.Connection to javafx.fxml;
    opens marc.View to javafx.fxml;
    opens marc.Class to javafx.fxml;
    opens marc.Controllers to javafx.fxml;  // Ensure this matches your actual package
}

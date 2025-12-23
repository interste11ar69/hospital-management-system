package marc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class       HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/marc/UI/GUI_F.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1250, 750);
            stage.setTitle("Hospital Management System");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading application: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

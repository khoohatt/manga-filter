import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    public void start(Stage primaryStage) {
        URL url = null;
        try {
            url = new File("src/main/java/scenes/login.fxml").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Finelait");
        Scene scene = new Scene(Objects.requireNonNull(root));
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        SceneController.addIcon(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
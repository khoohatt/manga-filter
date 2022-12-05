import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("src/main/java/scenes/login.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        //root.getStylesheets().add(Main.class.getResource("fontstyle.css").toString());
        primaryStage.setTitle("Finelait");

        /* Scene scene = new Scene(root);
        scene.getStylesheets().add("fontstyle.css");
        primaryStage.setScene(scene); */

        Scene scene = new Scene(root);


        //url = new File("src/main/resources/style.css").toURI().toURL();
        //String path = String.valueOf(url).replaceAll("%20", " ");
        scene.getStylesheets().add("style.css");

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        //primaryStage.setMaximized(true);

        /* url = new File("src/main/java/style.css").toURI().toURL();
        String path = String.valueOf(url).replaceAll("%20", " ");
        System.out.println(url);
        root.getStylesheets().add(path);
        //primaryStage.getScene().getStylesheets().add(path); */
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


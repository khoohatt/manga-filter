import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import javafx.scene.control.Button;

public class SceneController {
    public static void changeWindow(Button button, String file, String sceneName, boolean closeWindow) {
        if (closeWindow) {
            button.getScene().getWindow().hide();
        }

        URL url = null;
        try {
            url = new File("src/main/java/scenes/" + file + ".fxml").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Finelait - " + sceneName);
        stage.setScene(new Scene(Objects.requireNonNull(root)));
        stage.setResizable(false);
        stage.show();
    }

    public static void changeScene(Button button, String file, String sceneName) {
        Stage stage = (Stage) button.getScene().getWindow();

        String urlString = "src/main/java/scenes/" + file + ".fxml";

        URL url = null;
        try {
            url = new File(urlString).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Finelait - " + sceneName);
        //stage.getScene().getStylesheets().add("src/main/java/fontstyle.css");
        stage.getScene().getStylesheets().add("style.css");
        stage.setScene(new Scene(Objects.requireNonNull(root)));
        stage.setResizable(false);
        stage.show();
    }

    public static void openUserProfile(int num) {
        URL url = null;
        try {
            url = new File("src/main/java/scenes/profile" + num + ".fxml").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Finelait - профиль пользователя");
        stage.setScene(new Scene(Objects.requireNonNull(root)));
        stage.setResizable(false);
        stage.show();
    }

    public static Font getFont(int size) {
        URL url = null;
        try {
            url = new File("src/main/java/fonts/ofontru_AnimeAcev05.ttf").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String path = String.valueOf(url).replaceAll("%20", " ");

        return Font.loadFont(path, size);
    }
}

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

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
        addIcon(stage);
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
        stage.getScene().getStylesheets().add("style.css");
        stage.setScene(new Scene(Objects.requireNonNull(root)));
        stage.setResizable(false);
        addIcon(stage);
        stage.show();
    }

    public static void openUserProfile() {
        int num = (int) (Math.random()*(4) + 1);
        String urlString = "src/main/java/scenes/profiles/profile" + num + ".fxml";

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

        Stage stage = new Stage();
        stage.setTitle("Finelait - профиль пользователя");
        stage.setScene(new Scene(Objects.requireNonNull(root)));
        stage.setResizable(false);
        addIcon(stage);
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

    public static void addIcon(Stage stage) {
        URL url = null;
        try {
            url = new File("src/main/java/images/icon.png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        stage.getIcons().add(new Image(String.valueOf(url)));
    }
}
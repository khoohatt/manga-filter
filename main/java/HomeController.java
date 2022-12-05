import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeController {

    public static User user;
    @FXML
    public Button filButton, setButton, infoButton;
    public ImageView homeImage;

    @FXML
    void initialize() {
        filButton.setOnAction(e -> SceneController.changeScene(filButton, "filter", "поиск"));
        setButton.setOnAction(e -> SceneController.changeScene(setButton, "settings", "настройки"));
        infoButton.setOnAction(e -> SceneController.changeScene(infoButton, "info", "информация"));

        URL url = null;
        try {
            url = new File("src/main/java/images/home.jpg").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        homeImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        Font font = SceneController.getFont(12);
        filButton.setFont(font);
        setButton.setFont(font);
        infoButton.setFont(font);
    }

    public static User getUser() {
        return user;
    }
}

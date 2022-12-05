import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class InfoController {
    public Button backButton;
    public ImageView qrImage, infoImage;
    public Label mainLabel, ballonLabel;

    public void initialize() {
        URL url = null;
        try {
            url = new File("src/main/java/images/info.jpg").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        infoImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        try {
            url = new File("src/main/java/images/qr.jpg").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        qrImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        Font font = SceneController.getFont(12);
        Font balFont = SceneController.getFont(7);
        backButton.setFont(font);
        mainLabel.setFont(font);
        ballonLabel.setFont(balFont);

        backButton.setOnAction(e -> SceneController.changeScene(backButton, "home", "добро пожаловать"));
    }
}

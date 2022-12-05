import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileController {
    public static int id;
    @FXML
    public Label nameLabel, specLabel, contLabel;
    public ImageView profImage;

    public void initialize() throws SQLException {
        createScene();

        System.out.println(id);
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet result = dbHandler.getUserById(id);
        while (result.next()) {
            nameLabel.setText("Профиль пользователя " + result.getString(Const.USERS_NAME));
            //specLabel.setText(result.getString(Const.USERS_NAME));
            contLabel.setText(result.getString(Const.USERS_CONTACT));
        }
    }
    public void createScene() {
        int num = (int) (Math.random()*3);
        SceneController.openUserProfile(num);
        URL url = null;
        try {
            url = new File("src/main/java/images/profiles/profile" + num + ".jpg").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        profImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));
    }
}

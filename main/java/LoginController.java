import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    public ImageView loginImage;
    public Button authButton, loginButton;
    public TextField loginField;
    public PasswordField passField;
    public Label hintLabel;

    @FXML
    void initialize() {
        Font font = SceneController.getFont(12);
        authButton.setFont(font);
        loginButton.setFont(font);
        passField.setFont(font);
        loginField.setFont(font);
        Font hintFont = SceneController.getFont(10);
        hintLabel.setFont(hintFont);

        URL url = null;
        try {
            url = new File("src/main/java/images/login1.png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        loginImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        authButton.setOnAction(event -> {
            try {
                loginUser();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        loginButton.setOnAction(e -> SceneController.changeWindow(loginButton, "register", "регистрация", true));
    }

    public void loginUser() throws SQLException {
        String login = loginField.getText().trim();
        String pass = passField.getText().trim();

        if (login.equals("")) {
            hintLabel.setVisible(true);
            Shake anim = new Shake(loginField);
            anim.playAnim();
            return;
        } if (pass.equals("")) {
            hintLabel.setVisible(true);
            Shake anim = new Shake(passField);
            anim.playAnim();
            return;
        }

        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setName(login);
        user.setPass(pass);
        ResultSet result = dbHandler.getUser(user);

        if (result.next()) {
            user.setId(result.getInt(Const.USERS_ID));
            System.out.println("успешно.");
            HomeController.user = user;
            SceneController.changeWindow(loginButton, "home", "добро пожаловать", true);
            return;
        }

        hintLabel.setVisible(true);
        System.out.println("неверный пароль или логин.");
        Shake loginAnim = new Shake(loginField);
        Shake passAnim = new Shake(passField);
        loginAnim.playAnim();
        passAnim.playAnim();
    }
}
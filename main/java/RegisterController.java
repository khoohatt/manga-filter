import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

public class RegisterController {
    @FXML
    public DatePicker datePicker;
    public ImageView regImage;
    public TextField nameField;
    public PasswordField passField;
    public Button registerButton;
    public Label hintLabel;

    @FXML
    void initialize() {
        Font font = SceneController.getFont(12);
        passField.setFont(font);
        registerButton.setFont(font);
        nameField.setFont(font);
        passField.setFont(font);
        Font hintFont = SceneController.getFont(10);
        hintLabel.setFont(hintFont);

        URL url = null;
        try {
            url = new File("src/main/java/images/register1.png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        regImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        registerButton.setOnAction(e -> regNewUser());
    }

    public void regNewUser() {
        String name = nameField.getText().trim();
        String pass = passField.getText().trim();
        LocalDate date = datePicker.getValue();

        if (name.equals("")) {
            hintLabel.setVisible(true);
            Shake anim = new Shake(nameField);
            anim.playAnim();
            return;
        } if (pass.equals("")) {
            hintLabel.setVisible(true);
            Shake anim = new Shake(passField);
            anim.playAnim();
            return;
        } if (date == null) {
            hintLabel.setVisible(true);
            Shake anim = new Shake(datePicker);
            anim.playAnim();
            return;
        }

        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = dbHandler.registerUser(name, pass, date);
        if (user != null) {
            HomeController.user = user;
            SceneController.changeWindow(registerButton, "home", "добро пожаловать", true);
        } else {
            hintLabel.setVisible(true);
            Shake anim = new Shake(nameField);
            anim.playAnim();
        }
    }
}
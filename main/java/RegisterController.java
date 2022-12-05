import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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

    @FXML
    private TextField contField, nameField;

    @FXML
    private PasswordField passField;

    @FXML
    private Button registerButton;

    @FXML
    void initialize() {
        URL url = null;
        try {
            url = new File("src/main/java/images/register1.png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        regImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        registerButton.setOnAction(event -> {
            regNewUser();
        });

        Font font = SceneController.getFont(12);
        passField.setFont(font);
        registerButton.setFont(font);
        nameField.setFont(font);
        passField.setFont(font);
        contField.setFont(font);
    }

    private void regNewUser() {
        String name = nameField.getText().trim();
        String cont = contField.getText().trim();
        String pass = passField.getText().trim();
        LocalDate date = datePicker.getValue();

        boolean flag = false;
        if (name.equals("")) {
            Shake anim = new Shake(nameField);
            anim.playAnim();
            flag = true;
        } if (pass.equals("")) {
            Shake anim = new Shake(passField);
            anim.playAnim();
            flag = true;
        } if (date == null) {
            Shake anim = new Shake(datePicker);
            anim.playAnim();
            flag = true;
        }
        if (flag) {
            return;
        }

        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = dbHandler.registerUser(name, cont, pass, date);
        if (user!= null) {
            HomeController.user = user;
            SceneController.changeWindow(registerButton, "home", "добро пожаловать", true);
        } else {
            Shake anim = new Shake(nameField);
            anim.playAnim();
        }
    }
}
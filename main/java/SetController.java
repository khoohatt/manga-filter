import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SetController {
    @FXML
    public DatePicker datePicker;
    public Button backButton, changeButton;
    public GridPane gridPane;
    @FXML
    public TextField exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8, exp9, exp10, exp11, exp12;
    @FXML
    public CheckBox spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8, spec9, spec10, spec11, spec12;

    @FXML
    public TextField contField, infoField, nameField;
    public ImageView settingsImage;

    @FXML
    private PasswordField passField;

    @FXML
    public Label rightsLabel;

    @FXML
    void initialize() throws SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet result = dbHandler.getUser(HomeController.getUser());
        int id = HomeController.getUser().getId();
        while (result.next()) {
            System.out.println(result.getString(Const.USERS_NAME));
            contField.setText(result.getString(Const.USERS_CONTACT));
            passField.setText(result.getString(Const.USERS_PASS));
            datePicker.setValue(result.getDate(Const.USERS_AGE).toLocalDate());
            int userRights = result.getInt(Const.USERS_RIGHT);
            if (userRights == 1) {
                rightsLabel.setText("Настройки модератора...");
            } else if (userRights == 2) {
                rightsLabel.setText("Настройки администратора...");
            } else {
                rightsLabel.setText("Настройки пользователя...");
            }
        }

        ResultSet specsResult = dbHandler.getSpecsForProfile(id);
        List<CheckBox> specsChecks = Arrays.asList(spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8, spec9, spec10, spec11, spec12);
        List<TextField> expsFields = Arrays.asList(exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8, exp9, exp10, exp11, exp12);
        while (specsResult.next()) {
            int spec = specsResult.getInt(Const.TAG_SPEC);
            specsChecks.get(spec - 1).setSelected(true);
            int exp = specsResult.getInt(Const.TAG_EXP);
            expsFields.get(spec - 1).setText(String.valueOf(exp));
        }

        URL url = null;
        try {
            url = new File("src/main/java/images/set.jpg").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        settingsImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        backButton.setOnAction(e -> SceneController.changeScene(backButton, "home", "добро пожаловать"));

        Font font = SceneController.getFont(12);
        Font specFont = SceneController.getFont(10);
        for (int i = 0; i < 12; i++) {
            specsChecks.get(i).setFont(specFont);
            expsFields.get(i).setFont(specFont);
        }
        nameField.setFont(font);
        contField.setFont(font);
        rightsLabel.setFont(font);
        passField.setFont(font);
        infoField.setFont(font);
        backButton.setFont(font);
        changeButton.setFont(font);
    }

    public void changeInfo() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        int id = HomeController.getUser().getId();

        List<CheckBox> specsChecks = Arrays.asList(spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8, spec9, spec10, spec11, spec12);
        List<TextField> expsFields = Arrays.asList(exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8, exp9, exp10, exp11, exp12);
        int[] specs = new int[12];
        for (int i = 0; i < specsChecks.size(); i++) {
            if (specsChecks.get(i).isSelected()) {
                if (!expsFields.get(i).getText().equals("")) {
                    specs[i] = Integer.parseInt(expsFields.get(i).getText());
                } else {
                    specs[i] = 0;
                }
            } else {
                specs[i] = -1;
            }
            System.out.println("spec " + (i + 1) + " exp " + specs[i]);
        }
        User user = new User();

        dbHandler.deleteSpecs(id);
        dbHandler.updateUser(id, contField.getText(), passField.getText(), datePicker.getValue());

        boolean updated = dbHandler.updateName(id, nameField.getText());
        if ((!updated) && (!nameField.getText().equals(HomeController.getUser().getName()))) {
            Shake anim = new Shake(nameField);
            anim.playAnim();
            user.setName(HomeController.getUser().getName());
        } else {
            user.setName(nameField.getText());
        }

        dbHandler.updateUserSpecs(specs, id);
        user.setId(id);
        user.setPass(passField.getText());
        HomeController.user = user;
    }

    public void checkField (KeyEvent actionEvent) {
        TextField textField = (TextField) actionEvent.getSource();
        String string = textField.getText();
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            textField.clear();
            System.out.println("huh");
        }
    }

    public void showSpecs() {
        gridPane.setVisible(!gridPane.isVisible());
        //scrollPane.setLayoutY(97);
        //scrollPane.setLayoutX(23);
    }
}

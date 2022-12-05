import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterController {

    @FXML
    public ScrollPane scrollPane;
    public TextField nameField, fromAge, toAge;
    public FlowPane flowPane;
    public Button backButton, findUsersButton;
    @FXML
    public TextField exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8, exp9, exp10, exp11, exp12;
    @FXML
    public CheckBox spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8, spec9, spec10, spec11, spec12;
    public ImageView filterImage;

    public void initialize() {
        flowPane.setOrientation(Orientation.VERTICAL);
        backButton.setOnAction(e -> SceneController.changeScene(backButton, "home", "добро пожаловать"));

        List<CheckBox> specsChecks = Arrays.asList(spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8, spec9, spec10, spec11, spec12);
        List<TextField> expsFields = Arrays.asList(exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8, exp9, exp10, exp11, exp12);

        URL url = null;
        try {
            url = new File("src/main/java/images/filter.jpg").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        filterImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        Font font = SceneController.getFont(12);
        Font specFont = SceneController.getFont(10);
        for (int i = 0; i < 12; i++) {
            specsChecks.get(i).setFont(specFont);
            expsFields.get(i).setFont(specFont);
        }
        nameField.setFont(font);
        fromAge.setFont(font);
        toAge.setFont(font);
        backButton.setFont(font);
        findUsersButton.setFont(font);
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

    public void findUsers() throws SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        LocalDate fromDate = null;
        LocalDate toDate = null;
        if (!fromAge.getText().equals("")) {
            fromDate = LocalDate.now().minusYears(Long.parseLong(fromAge.getText()));
        }
        if (!toAge.getText().equals("")) {
            toDate = LocalDate.now().minusYears(Long.parseLong(toAge.getText()));
        }

        List<CheckBox> specsChecks = Arrays.asList(spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8, spec9, spec10, spec11, spec12);
        List<TextField> expsFields = Arrays.asList(exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8, exp9, exp10, exp11, exp12);
        int[] specs = new int[12];
        boolean specSelected = false;
        for (int i = 0; i < specsChecks.size(); i++) {
            if (specsChecks.get(i).isSelected()) {
                specSelected = true;
                if (!expsFields.get(i).getText().equals("")) {
                    specs[i] = Integer.parseInt(expsFields.get(i).getText());
                } else {
                    specs[i] = 0;
                }
            } else {
                specs[i] = -1;
            }
        }

        ResultSet specsResult = null;
        if (specSelected) {
            specsResult = dbHandler.getProfilesBySpecs(specs);
        }

        ResultSet result = dbHandler.getProfiles(nameField.getText(), fromDate, toDate, specsResult);

        flowPane.getChildren().clear();
        if (result != null) {
            ArrayList<HBox> items = new ArrayList<>();

            while (result.next()) {
                String name = result.getString(Const.USERS_NAME);
                Label contLabel = new Label(String.valueOf(result.getString(Const.USERS_CONTACT)));
                Label dateLabel = new Label(getAgeString(LocalDate.parse(result.getString(Const.USERS_AGE))));
                Label nameLabel = new Label(name);

                int id = result.getInt(Const.USERS_ID);
                Button checkButton = new Button();
                checkButton.setOnAction(event -> {
                    ProfileController.id = id;
                    SceneController.changeWindow(checkButton, "profile", "профиль пользователя", false);
                });

                HBox hBox = new HBox(nameLabel, contLabel, dateLabel, checkButton);
                hBox.setSpacing(10);
                hBox.setStyle("HBox HBox.hgrow=\"ALWAYS\"");
                items.add(hBox);

                StringBuilder sb = new StringBuilder();
                ResultSet specResult = dbHandler.getSpecsForProfile(id);
                while (specResult.next()) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(specResult.getString(Const.TAG_SPEC)).append(" (").append(specResult.getInt(Const.TAG_EXP)).append(") ");
                }

                Label specLabel = new Label(String.valueOf(sb));
                GridPane gridpane = new GridPane();
                gridpane.getColumnConstraints().add(new ColumnConstraints(70));
                gridpane.getColumnConstraints().add(new ColumnConstraints(120));
                gridpane.getColumnConstraints().add(new ColumnConstraints(295));
                gridpane.getColumnConstraints().add(new ColumnConstraints(20));
                gridpane.getColumnConstraints().add(new ColumnConstraints(20));

                Font font = SceneController.getFont(7);
                List<Label> list = List.of(nameLabel, dateLabel, specLabel);
                for (Label label : list) {
                    label.setFont(font);
                }

                gridpane.add(nameLabel, 0, 0);
                gridpane.add(dateLabel, 1, 0);
                gridpane.add(specLabel, 2, 0);
                gridpane.add(checkButton, 3, 0);

                ResultSet resSet = dbHandler.getUser(HomeController.user);
                int right = 0;
                while (resSet.next()) {
                    right = resSet.getInt(Const.USERS_RIGHT);
                }
                if ((right == 1 && result.getInt(Const.USERS_RIGHT) == 0) || (right == 2)) {
                    Button banButton = new Button();
                    banButton.setOnAction(e -> {
                        dbHandler.deleteUser(id);
                    });
                    gridpane.add(banButton, 4, 0);
                }
                flowPane.getChildren().add(gridpane);
            }
            scrollPane.setContent(flowPane);
        }
    }

    public String getAgeString(LocalDate date) {
        int age = Period.between(date, LocalDate.now()).getYears();
        StringBuilder dateString = new StringBuilder().append(date).append(" (").append(age);
        int lastNum = age % 10;
        if ((lastNum) == 1) {
            dateString.append(" год)");
        } else if ((lastNum == 0) || (lastNum > 4)) {
            dateString.append(" лет)");
        } else {
            dateString.append(" года)");
        }
        return String.valueOf(dateString);
    }

    private void checkUser(int id, Button button) throws SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resSet = dbHandler.getSpecsForProfile(id);

        while (resSet.next()) {
            int spec = resSet.getInt(Const.TAG_SPEC);
            int exp = resSet.getInt(Const.TAG_USER);
        }
        //SceneController.openUserProfile();

        //pane.getChildren().add();
    }
}

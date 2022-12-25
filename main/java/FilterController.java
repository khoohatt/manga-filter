import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
    public TextField exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8, exp9, exp10, exp11, exp12;
    public CheckBox spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8, spec9, spec10, spec11, spec12;
    public ImageView filterImage;
    public Label ageLabel;
    public RadioButton selectBut;

    public void initialize() {
        URL url = null;
        try {
            url = new File("src/main/java/images/filter.jpg").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        filterImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        backButton.setOnAction(e -> SceneController.changeScene(backButton, "home", "добро пожаловать"));

        List<CheckBox> specsChecks = Arrays.asList(spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8, spec9, spec10, spec11, spec12);
        List<TextField> expsFields = Arrays.asList(exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8, exp9, exp10, exp11, exp12);

        Font font = SceneController.getFont(12);
        Font specFont = SceneController.getFont(10);
        for (int i = 0; i < 12; i++) {
            specsChecks.get(i).setFont(specFont);
            int count = i;
            specsChecks.get(i).setOnAction(e -> {
                if (selectBut.isSelected()) {
                    for (int j = 0; j < 12; j++) {
                        if (count != j) {
                            specsChecks.get(j).setSelected(!specsChecks.get(j).isSelected());
                        }
                    }
                }
            });
            expsFields.get(i).setFont(specFont);
        }
        nameField.setFont(font);
        fromAge.setFont(font);
        toAge.setFont(font);
        backButton.setFont(font);
        findUsersButton.setFont(font);
        ageLabel.setFont(font);
        selectBut.setFont(specFont);
    }

    public void checkField(KeyEvent actionEvent) {
        TextField textField = (TextField) actionEvent.getSource();
        String string = textField.getText();
        if (!string.equals("")) {
            try {
                Integer.parseInt(string);
            } catch (NumberFormatException e) {
                textField.deleteText(textField.getText().length() - 1, textField.getText().length());
            }
        }

        if (selectBut.isSelected()) {
            List<TextField> expsFields = Arrays.asList(exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8, exp9, exp10, exp11, exp12);
            for (int i = 0; i < 12; i++) {
                if (!expsFields.get(i).equals(textField)) {
                    expsFields.get(i).setText(string);
                }
            }
        }
        textField.selectEnd();
        textField.deselect();
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
        boolean hasSpec = false;
        if (specSelected) {
            specsResult = dbHandler.getProfilesBySpecs(specs);
            hasSpec = true;
        }

        ResultSet result = dbHandler.getProfiles(nameField.getText(), fromDate, toDate, specsResult, hasSpec);

        flowPane.getChildren().clear();
        if (result != null) {
            ArrayList<HBox> items = new ArrayList<>();

            while (result.next()) {
                URL url = null;
                try {
                    url = new File("src/main/java/images/openImg.jpg").toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                String name = result.getString(Const.USERS_NAME);
                Label contLabel = new Label(String.valueOf(result.getString(Const.USERS_CONTACT)));
                Label dateLabel = new Label(getAgeString(LocalDate.parse(result.getString(Const.USERS_AGE))));
                Label nameLabel = new Label(" " + name);

                int id = result.getInt(Const.USERS_ID);
                Button checkButton = new Button();
                checkButton.setOnAction(event -> {
                    ProfileController.id = id;
                    SceneController.openUserProfile();
                });

                HBox hBox = new HBox(nameLabel, contLabel, dateLabel, checkButton);
                hBox.setSpacing(10);
                hBox.setStyle("HBox HBox.hgrow=\"ALWAYS\"");
                items.add(hBox);

                ResultSet specResult = dbHandler.getSpecsForProfile(id);
                Label specLabel = new Label(getSpecString(specResult));

                GridPane gridpane = new GridPane();
                gridpane.getColumnConstraints().add(new ColumnConstraints(65));
                gridpane.getColumnConstraints().add(new ColumnConstraints(45));
                gridpane.getColumnConstraints().add(new ColumnConstraints(265));
                gridpane.getColumnConstraints().add(new ColumnConstraints(48));
                gridpane.getColumnConstraints().add(new ColumnConstraints(47));

                Font font = SceneController.getFont(7);
                List<Label> list = List.of(nameLabel, dateLabel, specLabel);
                for (Label label : list) {
                    label.setFont(font);
                }

                ImageView img = new ImageView(String.valueOf(url));

                Tooltip imgTip = new Tooltip("посмотреть профиль пользователя");
                imgTip.setFont(font);
                Tooltip.install(img, imgTip);

                img.setOnMouseClicked( e -> {
                    ProfileController.id = id;
                    SceneController.openUserProfile();
                });

                gridpane.add(nameLabel, 0, 0);
                gridpane.add(dateLabel, 1, 0);
                gridpane.add(specLabel, 2, 0);
                gridpane.add(img, 4, 0);

                ResultSet resSet = dbHandler.getUser(HomeController.user);
                int right = 0;
                while (resSet.next()) {
                    right = resSet.getInt(Const.USERS_RIGHT);
                }
                if ((right == 1 && result.getInt(Const.USERS_RIGHT) == 0) || (right == 2)) {
                    url = null;
                    try {
                        url = new File("src/main/java/images/banImg.jpg").toURI().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    ImageView banImg = new ImageView(String.valueOf(url));

                    Tooltip banTip = new Tooltip("удалить пользователя из базы");
                    banTip.setFont(font);
                    Tooltip.install(banImg, banTip);

                    banImg.setOnMouseClicked( e -> {
                        dbHandler.deleteUser(id);
                        flowPane.getChildren().clear();
                    });

                    gridpane.add(banImg, 3, 0);
                }
                flowPane.getChildren().add(gridpane);
            }
        } else {
            flowPane.getChildren().clear();
        }
        scrollPane.setContent(flowPane);
    }

    public String getSpecString(ResultSet specResult) throws SQLException {
        StringBuilder specString = new StringBuilder();
        List<String> specList = Arrays.asList("тайпер (текст)", "тайпер (звуки)", "клинер (цвет)", "клинер (ч/б)", "редактор", "переводчик (английский)", "переводчик (японский)", "переводчик (китайский)", "переводчик (корейский)", "координатор", "бета", "переводчик (другое)");
        while (specResult.next()) {
            if (specString.length() > 0) {
                specString.append(", ");
            }
            specString.append(specList.get(Integer.parseInt(specResult.getString(Const.TAG_SPEC)) - 1));

            int age = specResult.getInt(Const.TAG_EXP);
            if (age > 0) {
                specString.append(" (").append(age);
                int lastNum = age % 10;
                if ((lastNum) == 1) {
                    specString.append(" год)");
                } else if ((lastNum == 0) || (lastNum > 4)) {
                    specString.append(" лет)");
                } else {
                    specString.append(" года)");
                }
            }
        }
        return String.valueOf(specString);
    }

    public String getAgeString(LocalDate date) {
        int age = Period.between(date, LocalDate.now()).getYears();
        StringBuilder dateString = new StringBuilder().append(age);
        int lastNum = age % 10;
        if ((lastNum) == 1) {
            dateString.append(" год");
        } else if ((lastNum == 0) || (lastNum > 4)) {
            dateString.append(" лет");
        } else {
            dateString.append(" года");
        }
        return String.valueOf(dateString);
    }
}
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class ProfileController {
    public static int id;
    @FXML
    public Label nameLabel, specLabel, contLabel, expLabel, aboutLabel, ageLabel;
    public ImageView profImage;
    public TextField contText;
    @FXML
    private URL location;

    public void initialize() throws SQLException {
        String num = String.valueOf(location).substring(String.valueOf(location).length() - 6, String.valueOf(location).length() - 5);
        System.out.println(num);

        URL url = null;
        try {
            url = new File("src/main/java/images/profiles/profile" + num + ".jpg").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        profImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        StringBuilder sb = new StringBuilder();
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet result = dbHandler.getUserById(id);
        while (result.next()) {
            nameLabel.setText("Профиль пользователя " + result.getString(Const.USERS_NAME));
            contLabel.setText("контактная информация:\n");
            contText.setText(result.getString(Const.USERS_CONTACT));
            LocalDate date = LocalDate.parse(result.getString(Const.USERS_AGE));
            ageLabel.setText("Дата рождения: " + getAgeString(date));
            String info = result.getString(Const.USERS_ABOUT);
            if (info != null) {
                aboutLabel.setText(info);
            } else {
                aboutLabel.setText("пользователь ничего о себе не написал...");
            }
        }

        ResultSet specsResult = dbHandler.getSpecsForProfile(id);
        sb.setLength(0);
        while (specsResult.next()) {
            int spec = specsResult.getInt(Const.TAG_SPEC);
            int exp = specsResult.getInt(Const.TAG_EXP);
            sb.append(dbHandler.getSpecName(spec));
            if (exp != 0) {
                sb.append(", опыт: ").append(exp);
                int lastNum = exp % 10;
                if ((lastNum) == 1) {
                    sb.append(" год");
                } else if ((lastNum == 0) || (lastNum > 4)) {
                    sb.append(" лет");
                } else {
                    sb.append(" года");
                }
            }
            sb.append("\n");
        }
        if (sb.length() == 0) {
            sb.append("никаких данных не добавлено.");
        }
        expLabel.setText(String.valueOf(sb));

        Font font = SceneController.getFont(11);
        nameLabel.setFont(font);
        contLabel.setFont(font);
        aboutLabel.setFont(font);
        ageLabel.setFont(font);
        expLabel.setFont(font);
        specLabel.setFont(font);
        contText.setFont(font);
    }

    public String getAgeString(LocalDate date) {
        int age = Period.between(date, LocalDate.now()).getYears();
        StringBuilder dateString = new StringBuilder().append(date).append("\n(возраст - ").append(age);
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
}

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.text.Font;

public class LoginController {

    @FXML
    public ImageView loginImage;

    @FXML
    private Button authButton, loginButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passField;

    //public static String FONT_URL = "fonts/111anime-ace-v3.ttf";

    //private static Font createFont(String url) throws IOException, FontFormatException {
        //return Font.createFont(Font.TRUETYPE_FONT, LoginController.class.getClassLoader().getResourceAsStream(FONT_URL)).deriveFont(20, 30);
    //}

    @FXML
    void initialize() {
        /* URL urlll = null;
        try {
            urlll = new File("fonts/ofont.ru_AnimeAcev05.ttf").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String path = LoginController.class.getResource(String.valueOf(urlll)).getPath();
        Font myFontloadFontAirstreamNF20 = Font.loadFont(getClass()
                        .getResourceAsStream(String.valueOf(path)), 20);


        //Button btn = new Button();
        testLabel.setFont(myFontloadFontAirstreamNF20);*/
        //testLabel.setText("Say 'Hello World'");

        /* String path = "fonts/ofont.ru_AnimeAcev05.ttf";
        File file = null;
        try {
            URI u = new URI(path.trim().replaceAll("%20", "\u0020"));
            file = new File(u.getPath());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        //path =  LoginController.class.getResource(file).getPath()
        Font myFontloadFontAirstreamNF20 = Font.loadFont(getClass()
                .getResourceAsStream(Objects.requireNonNull(file).getAbsolutePath()), 20);
        testLabel.setFont(myFontloadFontAirstreamNF20);*/

        Font font = SceneController.getFont(12);
        authButton.setFont(font);
        loginButton.setFont(font);
        passField.setFont(font);
        loginField.setFont(font);

        URL url = null;
        try {
            url = new File("src/main/java/images/login1.png").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        loginImage.setImage(new javafx.scene.image.Image(String.valueOf(url)));

        //Font ace = null;
        /* try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Anime_Ace.ttf")));
            testLabel.setFont("Anime_Ace.ttf");
        } catch (IOException|FontFormatException e) {
            //Handle exception
        }

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    new File("Anime_Ace.ttf"));
            Font bold = font.deriveFont(Font.BOLD, 12);
            Font plain = font.deriveFont(Font.PLAIN, 12);
            //testLabel.setFont(bold);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        } */

        authButton.setOnAction(event -> {
            try {
                loginUser();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        loginButton.setOnAction(event -> {
            SceneController.changeWindow(loginButton, "register", "регистрация", true);
        });
    }

    private void loginUser() throws SQLException {
        String login = loginField.getText().trim();
        String pass = passField.getText().trim();

        boolean flag = false;
        if (login.equals("")) {
            Shake anim = new Shake(loginField);
            anim.playAnim();
            flag = true;
        } if (pass.equals("")) {
            Shake anim = new Shake(passField);
            anim.playAnim();
            flag = true;
        }
        if (flag) {
            return;
        }

        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setName(login);
        user.setPass(pass);
        ResultSet result = dbHandler.getUser(user);

        if (result.next()) {
            int id = result.getInt(Const.USERS_ID);
            user.setId(id);
            System.out.println("успешно.");
            HomeController.user = user;
            SceneController.changeWindow(loginButton, "home", "добро пожаловать", true);
            return;
        }

        System.out.println("неверный пароль или логин.");
        Shake loginAnim = new Shake(loginField);
        Shake passAnim = new Shake(passField);
        loginAnim.playAnim();
        passAnim.playAnim();
    }
}
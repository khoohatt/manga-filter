import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public class DatabaseHandler {
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "8734";
    private static final String URL = "jdbc:postgresql://localhost:5432/courseWork";

    Connection dbConnection = null;
    Driver driver;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        try {
            driver = new FabricMySQLDriver();
        } catch (SQLException e) {
            System.out.println("драйвер не найден.");
            return null;
        }

        try {
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            System.out.println("драйвер не зарегистрирован.");
            return null;
        }

        try {
            System.out.println("идет подключение к серверу...");
            dbConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("подключение произошло успешно.");
        } catch (SQLException e) {
            System.out.println("с сервером какие-то проблемы.");
            return null;
        }
        return dbConnection;
    }

    public void updateUser(int id, String cont, String pass, LocalDate date, boolean isHidden, String info) throws SQLException, ClassNotFoundException {
        String update = "UPDATE " + Const.USER_TABLE + " SET "
                + Const.USERS_CONTACT + "=?, " + Const.USERS_AGE + "=?, " + Const.USERS_VISIBILITY + "=?, "
                + Const.USERS_PASS + "=?, " + Const.USERS_ABOUT + "=? WHERE " + Const.USERS_ID + "=?";

        PreparedStatement pStatement = getDbConnection().prepareStatement(update);
        pStatement.setString(1, cont);
        pStatement.setDate(2, Date.valueOf(date));
        pStatement.setBoolean(3, isHidden);
        pStatement.setString(4, pass);
        pStatement.setString(5, info);
        pStatement.setInt(6, id);

        System.out.println(update);

        int rowsUpdated = pStatement.executeUpdate();
    }

    public boolean updateName(int id, String name) throws SQLException, ClassNotFoundException {
        String selectTableSQL = "SELECT " + Const.USERS_ID + ", " + Const.USERS_NAME + " from " + Const.USER_TABLE;
        try {
            Statement statement = getDbConnection().createStatement();
            ResultSet rs = statement.executeQuery(selectTableSQL);
            while (rs.next()) {
                String userid = rs.getString(Const.USERS_ID);
                String username = rs.getString(Const.USERS_NAME);
                if (username.equals(name.toLowerCase(Locale.ROOT))) {
                    System.out.println("такой логин уже существует.");
                    return false;
                }
                System.out.println("userid : " + userid);
                System.out.println("username : " + username.toLowerCase(Locale.ROOT));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        String update = "UPDATE " + Const.USER_TABLE + " SET " + Const.USERS_NAME + "=? WHERE " + Const.USERS_ID + "=?";

        PreparedStatement pStatement = getDbConnection().prepareStatement(update);
        pStatement.setString(1, name.toLowerCase(Locale.ROOT));
        pStatement.setInt(2, id);

        int rowsUpdated = pStatement.executeUpdate();
        return true;
    }

    public void updateUserSpecs(int[] specs, int id) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO " + Const.TAG_TABLE + "(" + Const.TAG_USER + "," + Const.TAG_SPEC + "," + Const.TAG_EXP + ")" + "VALUES(?,?,?)";
        PreparedStatement pStatement = getDbConnection().prepareStatement(insert);
        for (int i = 0; i < specs.length; i++) {
            if (specs[i] != -1) {
                System.out.println("дисциплина " + i);
                pStatement.setInt(1, id);
                pStatement.setInt(2, i + 1);
                pStatement.setInt(3, specs[i]);
                pStatement.executeUpdate();
                pStatement.clearParameters();
            }
        }
    }

    public void deleteSpecs(int id) throws SQLException, ClassNotFoundException {
        String delete = "DELETE FROM " + Const.TAG_TABLE + " * WHERE " + Const.TAG_USER + "='" + id + "'";
        PreparedStatement pStatement = getDbConnection().prepareStatement(delete);
        pStatement.executeUpdate();
    }

    public User registerUser(String name, String pass, LocalDate date) {
        String selectTableSQL = "SELECT " + Const.USERS_ID + ", " + Const.USERS_NAME + " from " + Const.USER_TABLE;
        User user;
        int id = 0;
        int buffId;

        try {
            Statement statement = getDbConnection().createStatement();
            ResultSet rs = statement.executeQuery(selectTableSQL);
            while (rs.next()) {
                buffId = rs.getInt(Const.USERS_ID);
                String userid = rs.getString(Const.USERS_ID);
                String username = rs.getString(Const.USERS_NAME);
                if (username.equals(name.toLowerCase(Locale.ROOT))) {
                    System.out.println("такой логин уже существует.");
                    return null;
                }
                System.out.println("userid : " + userid);
                System.out.println("username : " + username);
                if (buffId > id) {
                    id = buffId;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        String insert = "INSERT INTO " + Const.USER_TABLE + "(" + Const.USERS_NAME + "," + Const.USERS_PASS + "," + Const.USERS_AGE + "," + Const.USERS_VISIBILITY + ")" + "VALUES(?,?,?,?)";
        System.out.println(insert);

        try {
            PreparedStatement pStatement = getDbConnection().prepareStatement(insert);
            pStatement.setString(1, name.toLowerCase(Locale.ROOT));
            pStatement.setString(2, pass);
            pStatement.setDate(3, java.sql.Date.valueOf(date));
            pStatement.setBoolean(4, false);
            pStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        user = new User(++id, name.toLowerCase(Locale.ROOT), pass, date);
        return user;
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USERS_NAME + "=? AND " + Const.USERS_PASS + "=?";
        try {
            PreparedStatement pStatement = getDbConnection().prepareStatement(select);
            pStatement.setString(1, user.getName());
            pStatement.setString(2, user.getPass());
            resSet = pStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public ResultSet getUserById(int id) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USERS_ID + "=?";
        try {
            PreparedStatement pStatement = getDbConnection().prepareStatement(select);
            pStatement.setInt(1, id);
            resSet = pStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public ResultSet getProfiles(String name, LocalDate fromDate, LocalDate toDate, ResultSet resSet, boolean hasSpec) throws SQLException {
        ResultSet result = null;
        StringBuilder select = new StringBuilder();

        select.append("SELECT * FROM ").append(Const.USER_TABLE).append(" WHERE ").append(Const.USERS_VISIBILITY).append("='false'");
        if (!name.equals("")) {
            select.append(" AND ").append(Const.USERS_NAME).append("='").append(name.toLowerCase(Locale.ROOT)).append("'");
        }
        if (fromDate != null) {
            select.append(" AND ").append(Const.USERS_AGE).append("<'").append(Date.valueOf(fromDate)).append("'");
        }
        if (toDate != null) {
            select.append(" AND ").append(Const.USERS_AGE).append(">'").append(Date.valueOf(toDate)).append("'");
        }
        System.out.println(select);

        if (hasSpec) {
            if (resSet.next()) {
                select.append(" AND ");
                boolean addFilter = false;
                do {
                    if (addFilter) {
                        select.append(" OR ");
                    }
                    select.append("(").append(Const.USERS_ID).append("='").append(resSet.getInt(Const.TAG_USER)).append("'").append(")");
                    addFilter = true;
                } while (resSet.next());
            } else {
                return null;
            }
        }

        try {
            PreparedStatement pStatement = getDbConnection().prepareStatement(String.valueOf(select));
            result = pStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ResultSet getProfilesBySpecs(int[] specs) {
        ResultSet resSet = null;
        boolean addFilter = false;
        StringBuilder select = new StringBuilder();
        select.append("SELECT * FROM ").append(Const.TAG_TABLE);

        for (int i = 0; i < specs.length; i++) {
            if (specs[i] > -1) {
                if (addFilter) {
                    select.append(" OR ");
                } else {
                    select.append(" WHERE ");
                    addFilter = true;
                }
                select.append("(").append(Const.TAG_SPEC).append("='").append(i + 1).append("'");
                if (specs[i] > 0) {
                    select.append(" AND ").append(Const.TAG_EXP).append(">=").append(specs[i]);
                }
                select.append(")");
            }
        }
        System.out.println(select);

        try {
            PreparedStatement pStatement = getDbConnection().prepareStatement(String.valueOf(select));
            resSet = pStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (addFilter) {
            return resSet;
        } else {
            return null;
        }
    }

    public ResultSet getSpecsForProfile(int id) {
        ResultSet resSet = null;
        StringBuilder select = new StringBuilder();
        select.append("SELECT * FROM ").append(Const.TAG_TABLE).append(" WHERE ").append(Const.TAG_USER).append("='").append(id).append("'");
        System.out.println(select);

        try {
            PreparedStatement pStatement = getDbConnection().prepareStatement(String.valueOf(select));
            resSet = pStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public String getSpecName(int spec) throws SQLException {
        ResultSet resSet = null;
        StringBuilder select = new StringBuilder();
        select.append("SELECT * FROM ").append(Const.SPEC_TABLE).append(" WHERE ").append(Const.SPEC_ID).append("='").append(spec).append("'");
        System.out.println(select);

        try {
            PreparedStatement pStatement = getDbConnection().prepareStatement(String.valueOf(select));
            resSet = pStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        while (resSet.next()) {
            return Objects.requireNonNull(resSet).getString(Const.SPEC_NAME);
        }
        return  null;
    }

    public void deleteUser(int id) {
        StringBuilder select = new StringBuilder();
        select.append("DELETE FROM ").append(Const.USER_TABLE).append(" WHERE ").append(Const.USERS_ID).append("='").append(id).append("'");
        System.out.println(select);

        try {
            PreparedStatement pStatement = getDbConnection().prepareStatement(String.valueOf(select));
            pStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("запрос не вернул результата. но так и задумано.");
        }
    }
}
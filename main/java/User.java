import java.time.LocalDate;

public class User {
    private String name;
    private String pass;
    private String contacts;
    private Integer id;
    private LocalDate date;

    public User(Integer id, String name, String pass, LocalDate date) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.date = date;
    }

    public User() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

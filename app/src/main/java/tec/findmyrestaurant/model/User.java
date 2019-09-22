package tec.findmyrestaurant.model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String name;
    private String password;
    private char type;

    public static final char ADMIN = 'A',REGULAR='R';


    public User(){}

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public char getType() {
        return type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(char type) {
        this.type = type;
    }
}

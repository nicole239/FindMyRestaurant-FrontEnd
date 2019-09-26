package tec.findmyrestaurant.api;

public class Message {
    private boolean auth;
    private String token;
    private String message;
    private int id;
    private String guid;

    public Message(){}

    public Message(String message){
        this.message=message;
    }

    public boolean isAuth() {
        return auth;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public String getGuid() {
        return guid;
    }
}

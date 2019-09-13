package tec.findmyrestaurant.model;

public class Comment {

    private int idRestaurant;
    private String usrEmail;
    private float calification;

    public Comment(int idRestaurant, String usrEmail, float calification) {
        this.idRestaurant = idRestaurant;
        this.usrEmail = usrEmail;
        this.calification = calification;
    }

    public int getIdRestaurant() {
        return idRestaurant;
    }

    public String getUsrEmail() {
        return usrEmail;
    }

    public float getCalification() {
        return calification;
    }
}

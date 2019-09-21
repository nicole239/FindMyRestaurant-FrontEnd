package tec.findmyrestaurant.model;

import com.google.gson.annotations.SerializedName;

public class Calification {
    @SerializedName("idrestaurant")
    private int idRestaurant;
    private float calification;
    private User user;

    public int getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(int idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public float getCalification() {
        return calification;
    }

    public void setCalification(float calification) {
        this.calification = calification;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

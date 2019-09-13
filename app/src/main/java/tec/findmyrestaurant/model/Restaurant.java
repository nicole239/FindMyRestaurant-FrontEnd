package tec.findmyrestaurant.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Restaurant implements Serializable {

    private int idRestaurant;
    private String name;
    private float latitude;
    private float longitude;
    private String shedule;
    private String foodType;
    private int phoneNumber;
    private String website;
    private int price;
    private User usrCreator;
    private ArrayList<String> photos;

    public void Restaurant(){
        photos = new ArrayList<>();
    }

    public int getIdRestaurant() {
        return idRestaurant;
    }

    public String getName() {
        return name;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getShedule() {
        return shedule;
    }

    public String getFoodType() {
        return foodType;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public int getPrice() {
        return price;
    }

    public User getUsrCreator() {
        return usrCreator;
    }

    public void setIdRestaurant(int idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setShedule(String shedule) {
        this.shedule = shedule;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setUsrCreator(User usrCreator) {
        this.usrCreator = usrCreator;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public void addPhoto(String photo){
        photos.add(photo);
    }
}

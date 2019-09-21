package tec.findmyrestaurant.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Serializable {

    @SerializedName("idfoodtype")
    private int idRestaurant;
    private String name;
    private float latitude;
    private float longitude;
    private String schedule;
    @SerializedName("foodtype")
    private FoodType foodType;
    @SerializedName("phonenumber")
    private int phoneNumber;
    private String website;
    private int price;
    private User usrCreator;
    private List<String> photos;

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

    public String getSchedule() {
        return schedule;
    }

    public FoodType getFoodType() {
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

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setFoodType(FoodType foodType) {
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

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public void addPhoto(String photo){
        photos.add(photo);
    }
}

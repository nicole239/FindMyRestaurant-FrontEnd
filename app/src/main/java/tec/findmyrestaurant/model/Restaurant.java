package tec.findmyrestaurant.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Serializable {

    @SerializedName("idrestaurant")
    private int idRestaurant;
    private String name;
    private double latitude;
    private double longitude;
    private String schedule;
    @SerializedName("foodtype")
    private FoodType foodType;
    @SerializedName("phonenumber")
    private int phoneNumber;
    private String website;
    private char price;
    @SerializedName("user")
    private User usrCreator;
    private List<String> photos = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public void Restaurant(){
        photos = new ArrayList<>();
    }

    public int getIdRestaurant() {
        return idRestaurant;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
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

    public char getPrice() {
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
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

    public void setPrice(char price) {
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }
}

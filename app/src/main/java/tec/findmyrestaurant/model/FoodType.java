package tec.findmyrestaurant.model;

import com.google.gson.annotations.SerializedName;

public class FoodType {

    @SerializedName("idfoodtype")
    private int idFoodType;
    private String name;

    public int getIdfoodtype() {
        return idFoodType;
    }

    public String getName() {
        return name;
    }

    public void setIdfoodtype(int idfoodtype) {
        this.idFoodType = idfoodtype;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package tec.findmyrestaurant.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodType implements Serializable {

    @SerializedName("idfoodtype")
    private int idFoodType;
    @SerializedName("foodname")
    private String name;

    public FoodType(int idFoodType, String name) {
        this.idFoodType = idFoodType;
        this.name = name;
    }

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

package com.shm_rz.ufoodapp.Model;

/**
 * Created by Shabnam Moazam on 16/03/2018.
 */

public class Order {
    private String FoodID;
    private String FoodName;
    private String FoodCount;
    private String FoodPrice;
    private String FoodImage;
    private String ID;
    private String RestuarantName;
    private String ResID;

    public Order() {
    }

    public Order(String foodID, String foodName, String foodCount, String foodPrice, String foodImage, String ID, String restuarantName, String resID) {
        FoodID = foodID;
        FoodName = foodName;
        FoodCount = foodCount;
        FoodPrice = foodPrice;
        FoodImage = foodImage;
        this.ID = ID;
        RestuarantName = restuarantName;
        ResID = resID;
    }

    public String getResID() {
        return ResID;
    }

    public void setResID(String resID) {
        ResID = resID;
    }

    public String getRestuarantName() {
        return RestuarantName;
    }

    public void setRestuarantName(String restuarantName) {
        RestuarantName = restuarantName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        FoodID = foodID;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodCount() {
        return FoodCount;
    }

    public void setFoodCount(String foodCount) {
        FoodCount = foodCount;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }

    public String getFoodImage() {
        return FoodImage;
    }

    public void setFoodImage(String foodImage) {
        FoodImage = foodImage;
    }
}

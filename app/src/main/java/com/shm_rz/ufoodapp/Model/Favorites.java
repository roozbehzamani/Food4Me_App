package com.shm_rz.ufoodapp.Model;

/**
 * Created by Shabnam Moazam on 29/03/2018.
 */

public class Favorites {
    int FoodId;

    String
            FoodName,
            FoodPrice,
            FoodImage,
            FoodDiscount;

    public Favorites(int foodId, String foodName, String foodPrice, String foodImage, String foodDiscount) {
        FoodId = foodId;
        FoodName = foodName;
        FoodPrice = foodPrice;
        FoodImage = foodImage;
        FoodDiscount = foodDiscount;
    }

    public int getFoodId() {
        return FoodId;
    }

    public void setFoodId(int foodId) {
        FoodId = foodId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
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

    public String getFoodDiscount() {
        return FoodDiscount;
    }

    public void setFoodDiscount(String foodDiscount) {
        FoodDiscount = foodDiscount;
    }
}

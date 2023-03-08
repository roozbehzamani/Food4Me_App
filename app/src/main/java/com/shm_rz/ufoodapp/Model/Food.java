package com.shm_rz.ufoodapp.Model;

/**
 * Created by Shabnam Moazam on 15/03/2018.
 */

public class Food {

    int id , FoodCount;

    String
            cost           ,
            name ,
            User_Email     ,
            resID          ,
            CreateMaterial ,
            foodImage      ,
            Recipe         ,
            bakingTime     ,
            menuID         ;

    boolean OrderType;



    public String getBakingTime() {
        return bakingTime;
    }

    public void setBakingTime(String bakingTime) {
        this.bakingTime = bakingTime;
    }


    public String getRecipe() {
        return Recipe;
    }

    public void setRecipe(String recipe) {
        this.Recipe = recipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_Email() {
        return User_Email;
    }

    public void setUser_Email(String user_Email) {
        User_Email = user_Email;
    }

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public String getCreateMaterial() {
        return CreateMaterial;
    }

    public void setCreateMaterial(String createMaterial) {
        CreateMaterial = createMaterial;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getFoodCount() {
        return FoodCount;
    }

    public void setFoodCount(int foodCount) {
        FoodCount = foodCount;
    }

    public boolean isOrderType() {
        return OrderType;
    }

    public void setOrderType(boolean orderType) {
        OrderType = orderType;
    }
}

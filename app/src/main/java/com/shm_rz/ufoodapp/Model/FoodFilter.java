package com.shm_rz.ufoodapp.Model;

import java.util.Comparator;

/**
 * Created by Roozbeh Zamani on 16/06/2018.
 */

public class FoodFilter {

    public int id;
    public int cost;
    public String name;
    public String foodImage;
    public String restuarant;
    public String menuName;
    public String resID;
    public int FoodCount;
    public boolean OrderType;


    public static final Comparator<FoodFilter> BY_COST = new Comparator<FoodFilter>() {
        @Override
        public int compare(FoodFilter o1, FoodFilter o2) {

            return Integer.compare(o1.cost , o2.cost);
        }
    };

    public static final Comparator<FoodFilter> BY_COST_SECEND = new Comparator<FoodFilter>() {
        @Override
        public int compare(FoodFilter o1, FoodFilter o2) {
            return Integer.compare(o2.cost , o1.cost);
        }
    };

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getRestuarant() {
        return restuarant;
    }

    public void setRestuarant(String restuarant) {
        this.restuarant = restuarant;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
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

package com.shm_rz.ufoodapp.Model;

import java.util.List;

/**
 * Created by Shabnam on 09/01/2019.
 */

public class OrderItem {
    public int factorID;
    public List<Integer> foodCount;
    public List<Integer> foodID;

    public OrderItem() {
    }

    public OrderItem(int factorID, List<Integer> count, List<Integer> foodID) {
        this.factorID = factorID;
        this.foodCount = count;
        this.foodID = foodID;
    }

    public int getFactorID() {
        return factorID;
    }

    public void setFactorID(int factorID) {
        this.factorID = factorID;
    }

    public List<Integer> getCount() {
        return foodCount;
    }

    public void setCount(List<Integer> count) {
        this.foodCount = count;
    }

    public List<Integer> getFoodID() {
        return foodID;
    }

    public void setFoodID(List<Integer> foodID) {
        this.foodID = foodID;
    }
}

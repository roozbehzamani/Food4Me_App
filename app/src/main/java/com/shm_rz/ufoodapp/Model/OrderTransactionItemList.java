package com.shm_rz.ufoodapp.Model;

/**
 * Created by Shabnam on 22/10/2018.
 */

public class OrderTransactionItemList {
    public int ID;
    public int OrderCount;
    public String OrderName;
    public String OrderImage;

    public OrderTransactionItemList(int ID, int foodCount, String foodName, String foodImage) {
        this.ID = ID;
        this.OrderCount = foodCount;
        this.OrderName = foodName;
        this.OrderImage = foodImage;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }

    public String getOrderImage() {
        return OrderImage;
    }

    public void setOrderImage(String orderImage) {
        OrderImage = orderImage;
    }
}

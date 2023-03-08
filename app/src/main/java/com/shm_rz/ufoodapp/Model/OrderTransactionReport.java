package com.shm_rz.ufoodapp.Model;

import java.sql.Time;
import java.sql.Date;

/**
 * Created by Roozbeh Zamani on 03/10/2018.
 */

public class OrderTransactionReport {
    public int ID ;
    public int OrderPrice;
    public String resName;
    public String Status;
    public String OrderDate;
    public String OrderTime;
    public String resImage;

    public OrderTransactionReport(int ID, int totalPrice, String resName, String orderStatus, String orderDate, String orderTime, String resImage) {
        this.ID = ID;
        this.OrderPrice = totalPrice;
        this.resName = resName;
        this.Status = orderStatus;
        this.OrderDate = orderDate;
        this.OrderTime = orderTime;
        this.resImage = resImage;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getResImage() {
        return resImage;
    }

    public void setResImage(String resImage) {
        this.resImage = resImage;
    }
}

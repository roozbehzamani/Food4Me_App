package com.shm_rz.ufoodapp.Model;

import java.util.Date;

/**
 * Created by Roozbeh Zamani on 14/05/2018.
 */

public class Comment {
    int
            ID         ,
            foodID     ,
            parentID   ,
            cmLike     ,
            cmDislike  ,
            Stars      ;
    String
            name   ,
            email  ,
            text   ,
            phone  ;
    Date
            date ;
    boolean
            cmConfirm  ,
            cmRead     ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getCmLike() {
        return cmLike;
    }

    public void setCmLike(int cmLike) {
        this.cmLike = cmLike;
    }

    public int getCmDislike() {
        return cmDislike;
    }

    public void setCmDislike(int cmDislike) {
        this.cmDislike = cmDislike;
    }

    public int getStars() {
        return Stars;
    }

    public void setStars(int stars) {
        Stars = stars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCmConfirm() {
        return cmConfirm;
    }

    public void setCmConfirm(boolean cmConfirm) {
        this.cmConfirm = cmConfirm;
    }

    public boolean isCmRead() {
        return cmRead;
    }

    public void setCmRead(boolean cmRead) {
        this.cmRead = cmRead;
    }
}

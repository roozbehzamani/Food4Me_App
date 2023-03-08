package com.shm_rz.ufoodapp.Model;

/**
 * Created by Shabnam Moazam on 09/05/2018.
 */

public class Resturants {
    int ID;
    String resName ,
            resAddress ,
            resType ,
            resAvgServiceTime ,
            resLatLng ,
            resPhone ,
            resImage ,
            ResBusinessLicense,
            userEmail;

    boolean resEnable , StudentRes;

    float resPoints;

    public Resturants() {
    }

    public Resturants(int ID, String resName, String resAddress, String resType, String resAvgServiceTime, String resLatLng, String resPhone, String resImage, String resBusinessLicense, String userEmail, boolean resEnable, boolean studentRes, float resPoints) {
        this.ID = ID;
        this.resName = resName;
        this.resAddress = resAddress;
        this.resType = resType;
        this.resAvgServiceTime = resAvgServiceTime;
        this.resLatLng = resLatLng;
        this.resPhone = resPhone;
        this.resImage = resImage;
        ResBusinessLicense = resBusinessLicense;
        this.userEmail = userEmail;
        this.resEnable = resEnable;
        StudentRes = studentRes;
        this.resPoints = resPoints;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResAddress() {
        return resAddress;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getResAvgServiceTime() {
        return resAvgServiceTime;
    }

    public void setResAvgServiceTime(String resAvgServiceTime) {
        this.resAvgServiceTime = resAvgServiceTime;
    }

    public String getResLatLng() {
        return resLatLng;
    }

    public void setResLatLng(String resLatLng) {
        this.resLatLng = resLatLng;
    }

    public String getResPhone() {
        return resPhone;
    }

    public void setResPhone(String resPhone) {
        this.resPhone = resPhone;
    }

    public String getResImage() {
        return resImage;
    }

    public void setResImage(String resImage) {
        this.resImage = resImage;
    }

    public String getResBusinessLicense() {
        return ResBusinessLicense;
    }

    public void setResBusinessLicense(String resBusinessLicense) {
        ResBusinessLicense = resBusinessLicense;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isResEnable() {
        return resEnable;
    }

    public void setResEnable(boolean resEnable) {
        this.resEnable = resEnable;
    }

    public boolean isStudentRes() {
        return StudentRes;
    }

    public void setStudentRes(boolean studentRes) {
        StudentRes = studentRes;
    }

    public float getResPoints() {
        return resPoints;
    }

    public void setResPoints(float resPoints) {
        this.resPoints = resPoints;
    }
}
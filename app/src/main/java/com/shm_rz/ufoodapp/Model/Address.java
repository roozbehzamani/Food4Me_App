package com.shm_rz.ufoodapp.Model;

/**
 * Created by Shabnam Moazam on 26/05/2018.
 */

public class Address {

    String userEmail , Address , addressType , addressLatLng , apartmentName , Floor , Unit;
    int ID;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressLatLng() {
        return addressLatLng;
    }

    public void setAddressLatLng(String addressLatLng) {
        this.addressLatLng = addressLatLng;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getUserAddressID() {
        return ID;
    }

    public void setUserAddressID(int userAddressID) {
        this.ID = userAddressID;
    }
}

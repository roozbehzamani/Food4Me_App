package com.shm_rz.ufoodapp.Model;

/**
 * Created by Asus on 10/02/2019.
 */

public class CreditTransaction {

    public int ID;
    public String TransactionCode;
    public String Price;
    public String Date;
    public String Time;
    public boolean CreditType;

    public CreditTransaction() {
    }

    public CreditTransaction(int ID, String transactionCode, String price, String date, String time, boolean creditType) {
        this.ID = ID;
        TransactionCode = transactionCode;
        Price = price;
        Date = date;
        Time = time;
        CreditType = creditType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTransactionCode() {
        return TransactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        TransactionCode = transactionCode;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public boolean isCreditType() {
        return CreditType;
    }

    public void setCreditType(boolean creditType) {
        CreditType = creditType;
    }
}

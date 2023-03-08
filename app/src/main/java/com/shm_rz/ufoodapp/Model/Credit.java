package com.shm_rz.ufoodapp.Model;

/**
 * Created by Shabnam on 08/01/2019.
 */

public class Credit {
    int ID;
    int Credit;
    String Date;
    String Time;
    String UserEmail;
    String TransactionCode;
    String RootUser;

    public Credit() {
    }

    public Credit(int ID, int credit, String date, String time, String userEmail, String transactionCode, String rootUser) {
        this.ID = ID;
        Credit = credit;
        Date = date;
        Time = time;
        UserEmail = userEmail;
        TransactionCode = transactionCode;
        RootUser = rootUser;
    }

    public String getRootUser() {
        return RootUser;
    }

    public void setRootUser(String rootUser) {
        RootUser = rootUser;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCredit() {
        return Credit;
    }

    public void setCredit(int credit) {
        Credit = credit;
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

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getTransactionCode() {
        return TransactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        TransactionCode = transactionCode;
    }
}

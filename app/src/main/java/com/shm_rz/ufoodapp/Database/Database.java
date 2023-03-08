package com.shm_rz.ufoodapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.shm_rz.ufoodapp.Model.Favorites;
import com.shm_rz.ufoodapp.Model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shabnam Moazam on 16/03/2018.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "appFood.db" ;
    private static final int DB_VER = 2 ;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    public void AddToFlowerEventCartOrder(String EventCartID , String Sender , String Receiver , String Text , int ParentID) {
        SQLiteDatabase db = getReadableDatabase();
        String queryCart = String.format("INSERT INTO FlowerEventCart(EventCartID,Sender,Receiver,Text,ParentID) VALUES ('%s','%s','%s','%s','d');",
                EventCartID,
                Sender,
                Receiver,
                Text,
                ParentID
        );
        db.execSQL(queryCart);
    }


    //food cart
    public int getCountCart() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM orderDetail");
        Cursor cursor = db.rawQuery(query , null , null );
        if(cursor.moveToFirst()){
            do {
                count = cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        return count;
    }

    public ArrayList<Order> getCarts() {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {
                "FoodID"    ,
                "FoodName"  ,
                "FoodPrice"     ,
                "FoodCount"        ,
                "FoodImage",
                "ID" ,
                "RestuarantName",
                "ResID"
        };
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final ArrayList<Order> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(
                        new Order(
                                c.getString(c.getColumnIndex("FoodID")),
                                c.getString(c.getColumnIndex("FoodName")),
                                c.getString(c.getColumnIndex("FoodCount")),
                                c.getString(c.getColumnIndex("FoodPrice")),
                                c.getString(c.getColumnIndex("FoodImage")),
                                c.getString(c.getColumnIndex("ID")),
                                c.getString(c.getColumnIndex("RestuarantName")),
                                c.getString(c.getColumnIndex("ResID"))
                        ));
            }while (c.moveToNext());
        }
        return result;
    }

    public Order getSinglecart(String FoodID) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {
                "FoodID"    ,
                "FoodName"  ,
                "FoodPrice"     ,
                "FoodCount"        ,
                "FoodImage",
                "ID" ,
                "RestuarantName",
                "ResID"
        };
        String sqlTable = "OrderDetail";

        String query = String.format("SELECT * FROM OrderDetail WHERE FoodID='%s';" , FoodID);
        Cursor c = db.rawQuery(query , null);


        Order result = null;
        if(c.moveToFirst())
        {
            result = new Order(
                            c.getString(c.getColumnIndex("FoodID")),
                            c.getString(c.getColumnIndex("FoodName")),
                            c.getString(c.getColumnIndex("FoodCount")),
                            c.getString(c.getColumnIndex("FoodPrice")),
                            c.getString(c.getColumnIndex("FoodImage")),
                            c.getString(c.getColumnIndex("ID")),
                            c.getString(c.getColumnIndex("RestuarantName")),
                            c.getString(c.getColumnIndex("ResID"))
                    );
        }
        return result;
    }

    public void addToCart(String id , String name , String count , String price , String image , String orderID , String RestuarantName , String ResID) {
        SQLiteDatabase db = getReadableDatabase();
        String queryCart = String.format("INSERT INTO OrderDetail(FoodID,FoodName,FoodPrice,FoodCount,FoodImage,ID,RestuarantName,ResID) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');",
                id,
                name,
                price,
                count,
                image,
                orderID,
                RestuarantName,
                ResID
        );
        db.execSQL(queryCart);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String queryCart = String.format("DELETE FROM orderDetail");
        db.execSQL(queryCart);
    }

    public void updateCart(String counter , String id) {
        SQLiteDatabase db = getReadableDatabase();
        String queryCart = String.format("UPDATE orderDetail SET FoodCount = '%s' WHERE ID = '%s'" ,counter,id);
        db.execSQL(queryCart);
    }

    public void updateCartFoodID(String counter , String id) {
        SQLiteDatabase db = getReadableDatabase();
        String queryCart = String.format("UPDATE orderDetail SET FoodCount = '%s' WHERE FoodID = '%s'" ,counter,id);
        db.execSQL(queryCart);
    }

    public void updateFoodPrice(String price , String id) {
        SQLiteDatabase db = getReadableDatabase();
        String queryCart = String.format("UPDATE orderDetail SET FoodPrice = '%s' WHERE ID = '%s'" ,price,id);
        db.execSQL(queryCart);
    }

    public boolean FoodIsExist(String foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM OrderDetail WHERE FoodID='%s';" , foodId);
        Cursor cursor = db.rawQuery(query , null);
        if(cursor.getCount() <= 0 ){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public String FoodCount(String foodID){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM OrderDetail WHERE FoodID='%s';" , foodID);
        Cursor cursor = db.rawQuery(query , null);
        cursor.moveToFirst();
        String coun = cursor.getString(cursor.getColumnIndex("FoodCount"));
        return coun;
    }

    public void DeleteOrder(String id){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE ID = '%s'" , id);
        db.execSQL(query);
    }
    //end food cart

    //food favorites
    public void addToFavorites(String id , String name , String price , String image , String discount , String UserPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites (FoodId,UserPhone,FoodName,FoodPrice,FoodImage,FoodDiscount) VALUES('%s','%s','%s','%s','%s','%s');" ,
                id,
                UserPhone ,
                name,
                price,
                image,
                discount
        );
        db.execSQL(query);
    }

    public void removeFavorites(String foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodId='%s';" , foodId);
        db.execSQL(query);
    }

    public boolean isFavorites(String foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE FoodId='%s';" , foodId);
        Cursor cursor = db.rawQuery(query , null);
        if(cursor.getCount() <= 0 ){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<Favorites> getFavorite() {

        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"FoodId","FoodName","FoodPrice","FoodImage","FoodDiscount"};
        String sqlTable = "Favorites";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        ArrayList<Favorites> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Favorites(
                        c.getInt(c.getColumnIndex("FoodId")),
                        c.getString(c.getColumnIndex("FoodName")),
                        c.getString(c.getColumnIndex("FoodPrice")),
                        c.getString(c.getColumnIndex("FoodImage")),
                        c.getString(c.getColumnIndex("FoodDiscount"))
                ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void cleanFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        String queryCart = String.format("DELETE FROM Favorites");
        db.execSQL(queryCart);
    }
    //end food favorites
}


package com.shm_rz.ufoodapp.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.shm_rz.ufoodapp.Model.Address;
import com.shm_rz.ufoodapp.Model.CounterModel;
import com.shm_rz.ufoodapp.Model.Food;
import com.shm_rz.ufoodapp.Model.Resturants;
import com.shm_rz.ufoodapp.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roozbeh Zamani on 15/03/2018.
 */

public class Common {
    public static Activity privusePage;
    public static ArrayList<Context> firstPage = new ArrayList<>();
    public static String flowerID = "";
    public static String sweetID = "";
    public static int visibility = 0x00000004;
    public static int foodID ;
    public static String sex = "جنسیت";
    public static int currentAddressAdapterPossition;
    public static String currentFavoritesAdapterPossition;
    public static String comeFromCart = "";
    public static Address selectedAddress;
    public static int currentCartItemPossition;
    public static CounterModel counterModel;
    public static User currentUser;
    public static Food currentFood;
    private static final  String BASE_URL= "https://fcm.google.com/";
    public static String PHONE_TEXT = "userPhone";
    public  static  final String INTENT_FOOD_ID="foodId";
    public static String SelectedResNameForReserve;
    public static String ParentIDForFlowerCart ;
    public static int counterForSub = 0;
    public static int PositionForSub = 0;
    public static int PackagePrice = 0;
    public static Resturants resturants;
    public static String fromAddressMe = "";

    public static final String UPDATE = "ویرایش";
    public static final String DELETE = "حذف";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Passwoed";
    public static final String ACS_KEY = "Access";
    public static Bitmap scaleBitmap(Bitmap bitmap , int newWidth , int newHeght) {
        Bitmap scaleBitmap = Bitmap.createBitmap(newWidth , newHeght , Bitmap.Config.ARGB_8888);
        float scaleX = newWidth / (float)bitmap.getWidth();
        float scaleY = newHeght / (float)bitmap.getHeight();
        float pivotX = 0 , pivotY = 0;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX , scaleY , pivotX , pivotY);

        Canvas canvas = new Canvas(scaleBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap , 0 , 0 , new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaleBitmap;
    }

}

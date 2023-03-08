package com.shm_rz.ufoodapp.Service;



import com.shm_rz.ufoodapp.Model.Address;
import com.shm_rz.ufoodapp.Model.Comment;
import com.shm_rz.ufoodapp.Model.Credit;
import com.shm_rz.ufoodapp.Model.CreditTransaction;
import com.shm_rz.ufoodapp.Model.Food;
import com.shm_rz.ufoodapp.Model.FoodFilter;
import com.shm_rz.ufoodapp.Model.Menu;
import com.shm_rz.ufoodapp.Model.OrderItem;
import com.shm_rz.ufoodapp.Model.OrderTransactionItemList;
import com.shm_rz.ufoodapp.Model.OrderTransactionReport;
import com.shm_rz.ufoodapp.Model.Resturants;
import com.shm_rz.ufoodapp.Model.Student;
import com.shm_rz.ufoodapp.Model.User;

import java.math.BigInteger;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("Home/RegisterAPI")
    Call<Integer> insertData(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("mobNumber") String mob_phone,
            @Field("Access") String Access,
            @Field("Snumber") String Snumber,
            @Field("family") String family
    );

    @FormUrlEncoded
    @POST("Home/AdminForgetPassword")
    Call<Integer> AdminForgetPassword(
            @Field("Email") String Email,
            @Field("Phone") String Phone
    );

    @FormUrlEncoded
    @POST("Home/ForgetPasswordAPI")
    Call<Student> insertforget(@Field("Email") String email);

    @FormUrlEncoded
    @POST("Home/SignInAPI")
    Call<User> insertSingnIn(
            @Field("mobNumber") String mob_phone,
            @Field("password") String password,
            @Field("Access") String Access
    );


    @GET("Home/RestuarantListAPI")
    Call<List<Resturants>> getRestuarant();

    @GET("Home/FastFoodListAPI")
    Call<List<Resturants>> getFastFood();

    @GET("Home/TimeToEndUniversityCredit")
    Call<String> TimeToEndUniversityCredit();

    @GET("Home/TimeToOrder")
    Call<Integer> TimeToOrder();

    @GET("Home/TimeToUniCreditOrder")
    Call<Integer> TimeToUniCreditOrder();

    @GET("Home/CaffiShopListAPI")
    Call<List<Resturants>> getCaffiShop();

    @GET("Home/MenuResAPI")
    Call<List<Menu>> getResMenu();

    @GET("Home/MenuFastFoodAPI")
    Call<List<Menu>> getFastFoodMenu();

    @GET("Home/MenuCaffiShopAPI")
    Call<List<Menu>> getCaffiShopMenu();

    @FormUrlEncoded
    @POST("Home/FoodListAPI")
    Call<List<Food>> getFoodList(@Field("resId") int resId, @Field("menuId") int menuId);

    @FormUrlEncoded
    @POST("Home/SpecialResFood")
    Call<Food> SpecialResFood(@Field("foodID") int foodID);

    @FormUrlEncoded
    @POST("Home/DetailsFoodListAPI")
    Call<List<Food>> getFoodDeatail(@Field("id") int id);

    @FormUrlEncoded
    @POST("Home/CommentAPI")
    Call<Comment> insertComment(
            @Field("foodID") int foodID,
            @Field("stars") int stars,
            @Field("phone") String phone,
            @Field("text") String text
    );

    @FormUrlEncoded
    @POST("Home/CommentListAPI")
    Call<List<Comment>> getCommentList(@Field("id") int id);

    @FormUrlEncoded
    @POST("Home/CreditReportList")
    Call<List<CreditTransaction>> CreditReportList(@Field("userEmail") String userEmail);

    @FormUrlEncoded
    @POST("Home/ChangePasswordAPI")
    Call<Student> getChangePassword(@Field("newPassword") String newPassword, @Field("email") String email);

    @GET("Home/BannerAPI")
    Call<List<Food>> getBannerImage();


    @FormUrlEncoded
    @POST("Home/Rating")
    Call<Float> getFoodRating(@Field("id") int id);

    @FormUrlEncoded
    @POST("Home/ShowUserProfileAPI")
    Call<User> getprofile(@Field("userEmail") String userEmail);

    @FormUrlEncoded
    @POST("Home/EditUserProfileAPI")
    Call<String> editUserProfile(
            @Field("email") String email,
            @Field("name") String name,
            @Field("sex") String sex,
            @Field("birthDate") String birthDate,
            @Field("family") String family
    );

    @FormUrlEncoded
    @POST("Home/ShowUserAddressAPI")
    Call<List<Address>> getAddressMe(
            @Field("userPhone") String userPhone,
            @Field("userEmail") String userEmail
    );

    @FormUrlEncoded
    @POST("Home/InsertUserAddressAPI")
    Call<String> insertUserAddress(
            @Field("userEmail") String userEmail,
            @Field("address") String address,
            @Field("addressType") String addressType,
            @Field("latLng") String latLng,
            @Field("apartmentName") String apartmentName,
            @Field("floor") String floor,
            @Field("unit") String unit
    );

    @FormUrlEncoded
    @POST("Home/EditUserAddressAPI")
    Call<String> editUserAddress(
            @Field("id") int id,
            @Field("address") String address,
            @Field("addressType") String addressType,
            @Field("addressLatLng") String addressLatLng,
            @Field("apartmentName") String apartmentName,
            @Field("floor") String floor,
            @Field("unit") String unit
    );


    @GET("Home/ShowResMapAPI")
    Call<List<Resturants>> getAllRestuarant();

    @GET("Home/LoadingAPI")
    Call<Integer> Loading();

    @FormUrlEncoded
    @POST("Home/ActiveCodeAPI")
    Call<Integer> sendCode(@Field("activeCode") String activeCode, @Field("email") String email);

    @FormUrlEncoded
    @POST("Home/DeleteUserAddressAPI")
    Call<String> DeleteAddress(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("Home/ResSearch")
    Call<List<Resturants>> resSearch(@Field("resName") String resName);

    @GET("Home/FastFoodFilterApi")
    Call<List<FoodFilter>> getFastFoodFood();

    @GET("Home/ResFoodAPI")
    Call<List<FoodFilter>> getResFood();

    @GET("Home/CaffiShopFoodAPI")
    Call<List<FoodFilter>> getCaffiShopFood();

    @FormUrlEncoded
    @POST("Home/ResReservation")
    Call<String> ResReservation(
            @Field("date") Date date,
            @Field("time") Time time,
            @Field("tableNum") List<String> tableNum,
            @Field("userEmail") String userEmail,
            @Field("resName") String resName
    );


    @FormUrlEncoded
    @POST("Home/ResFoodListAPI")
    Call<List<Food>> ResAllFoodList(@Field("resName") String resName);


    @FormUrlEncoded
    @POST("Home/SingleUser")
    Call<User> SingleUser(@Field("email") String email);


    @FormUrlEncoded
    @POST("Home/CommentSweetsAPI")
    Call<Integer> CommentSweetsAPI(@Field("text") String text, @Field("phone") String phone, @Field("stars") int stars, @Field("SweetsId") int SweetsId);


    @FormUrlEncoded
    @POST("Home/PackageFactor")
    Call<Integer> PackageFactor(
            @Field("count") int count,
            @Field("foodID") int foodID,
            @Field("packageID") int packageID
    );

    @FormUrlEncoded
    @POST("Home/AlbumFactor")
    Call<Integer> AlbumFactor(
            @Field("count") int count,
            @Field("foodID") int foodID,
            @Field("packageID") int albumID
    );

    @FormUrlEncoded
    @POST("Home/CommentListAPI")
    Call<List<Comment>> getSweetsCommentList(@Field("id") int id);


    @FormUrlEncoded
    @POST("Home/ResReservationAPI")
    Call<Integer> ResReservationAPI(
            @Field("resID") int resID,
            @Field("Date") String Date,
            @Field("Time") String Time,
            @Field("tableID") int tableID,
            @Field("userEmail") String userEmail
    );

    @FormUrlEncoded
    @POST("Home/ResReservationListAPI")
    Call<Void> ResReservationListAPI(
            @Field("foodID") int foodID,
            @Field("foodCount") int foodCount,
            @Field("factorID") int factorID
    );

    @FormUrlEncoded
    @POST("Home/FoodReservationAPI")
    Call<Integer> FoodReservationAPI(
            @Field("resID") int resID,
            @Field("Date") String Date,
            @Field("Time") String Time,
            @Field("userEmail") String userEmail,
            @Field("orderType") String orderType
    );

    @FormUrlEncoded
    @POST("Home/FoodReservationListAPI")
    Call<Void> FoodReservationListAPI(
            @Field("foodID") int foodID,
            @Field("foodCount") int foodCount,
            @Field("factorID") int factorID
    );


    @FormUrlEncoded
    @POST("Home/FlowerReservationAPI")
    Call<Integer> FlowerReservationAPI(
            @Field("resID") int resID,
            @Field("Date") String Date,
            @Field("Time") String Time,
            @Field("userEmail") String userEmail,
            @Field("orderType") String orderType
    );

    @FormUrlEncoded
    @POST("Home/FlowerReservationListAPI")
    Call<Void> FlowerReservationListAPI(
            @Field("foodID") int foodID,
            @Field("foodCount") int foodCount,
            @Field("factorID") int factorID
    );

    @FormUrlEncoded
    @POST("Home/SweetReservationAPI")
    Call<Integer> SweetReservationAPI(
            @Field("resID") int resID,
            @Field("Date") String Date,
            @Field("Time") String Time,
            @Field("userEmail") String userEmail,
            @Field("orderType") String orderType,
            @Field("orderWeight") String orderWeight
    );

    @FormUrlEncoded
    @POST("Home/SweetReservationListAPI")
    Call<Void> SweetReservationListAPI(
            @Field("foodID") int foodID,
            @Field("factorID") int factorID
    );

    @FormUrlEncoded
    @POST("Home/CommentSweetsAPI")
    Call<Void> CommentSweetsAPI(
            @Field("foodID") int foodID
    );


    @FormUrlEncoded
    @POST("Home/CreateOrderFactor")
    Call<Integer> CreateOrderFactor(
            @Field("resID") int resID,
            @Field("userEmail") String userEmail,
            @Field("Comment") String Comment,
            @Field("addressID") int addressID,
            @Field("Credit") boolean Credit,
            @Field("delivery") boolean delivery,
            @Field("ReceveTime") String ReceveTime
    );

    @FormUrlEncoded
    @POST("Home/UpdateOrderFactor")
    Call<Integer> UpdateOrderFactor(
            @Field("id") int id,
            @Field("credit") String credit
    );

    @FormUrlEncoded
    @POST("Home/AddNewCredite")
    Call<Integer> AddNewCredite(
            @Field("UserEmail") String UserEmail,
            @Field("price") int price,
            @Field("code") String code,
            @Field("receveTime") String receveTime
    );


    @FormUrlEncoded
    @POST("Home/ShowCredit")
    Call<Credit> ShowCredit(
            @Field("userEmail") String userEmail
    );
    @FormUrlEncoded
    @POST("Home/ShowCreditOnlyM")
    Call<Credit> ShowCreditOnlyM(
            @Field("userEmail") String userEmail
    );

    @FormUrlEncoded
    @POST("Home/ShowCreditOnlyU")
    Call<Credit> ShowCreditOnlyU(
            @Field("userEmail") String userEmail
    );

    @POST("Home/CreateOrderFactorItem")
    Call<Integer> CreateOrderFactorItem(@Body OrderItem orderItem);


    @FormUrlEncoded
    @POST("Home/CreateAlbumFactorItem")
    Call<Integer> CreateAlbumFactorItem(
            @Field("id") int id,
            @Field("factorID") int OrderID
    );

    @FormUrlEncoded
    @POST("Home/CreatePackingFactorItem")
    Call<Integer> CreatePackingFactorItem(
            @Field("id") int id,
            @Field("factorID") int factorID
    );

    @FormUrlEncoded
    @POST("Home/ChechFoodCountForOrder")
    Call<Integer> ChechFoodCountForOrder(
            @Field("id") int id
    );
    @FormUrlEncoded
    @POST("Home/OrderFactorListAPI")
    Call<List<OrderTransactionReport>> OrderFactorListAPI(
            @Field("userEmail") String userEmail
    );

    @FormUrlEncoded
    @POST("Home/OrderItemListAPI")
    Call<List<OrderTransactionItemList>> OrderItemListAPI(
            @Field("factorID") int factorID
    );
}
package com.example.yoyo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("get.php")
    Call<String> getUserRegi(
            @Field("cart_data") String cartdata,
            @Field("user_data") String userdata
    );
    @FormUrlEncoded
    @POST("get.php")
    Call<String> getResult(
            @Field("track_order") String id
    );
}

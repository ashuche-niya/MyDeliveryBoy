package com.example.deliveryboy;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("delivery/pickedup/")
    Call<PickupResponse> getResponse(@Field("order_id") String order_id, @Field("status") String status);

    @FormUrlEncoded
    @POST("delivery/check/")
    Call<LoginResponse> getLogin(@Field("del_boy_id") String phone_no);

}

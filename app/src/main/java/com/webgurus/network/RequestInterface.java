package com.webgurus.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestInterface {

    @FormUrlEncoded
    @POST("api/v1/parent_update_profile")
    Call<JsonObject> getTextApi (@Field("id") String id,
                               @Field("update_last_name") String update_last_name,
                               @Field("mobile_number") String mobile_number,
                               @Field("update_mobile_number") String update_mobile_number,
                               @Field("api_key") String api_key,
                               @Field("device_type") String device_type,
                               @Field("update_first_name") String update_first_name) ;


}
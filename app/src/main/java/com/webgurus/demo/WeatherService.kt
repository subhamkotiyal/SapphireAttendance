package com.app.demo2

import retrofit2.Call
import retrofit2.http.*


interface WeatherService {

    @GET("data/2.5/weather?")
    fun getCurrentWeatherData(@Query("lat") lat: String, @Query("lon") lon: String, @Query("APPID") app_id: String): Call<WeatherResponse>


    @FormUrlEncoded
    @POST("api/v1/parent_update_profile?")
    fun updateProfile(
        @Field("id") id: String?,
        @Field("update_last_name") update_last_name: String?,
        @Field("mobile_number") mobile_number: String?,
        @Field("update_mobile_number") update_mobile_number: String?,
        @Field("api_key") api_key: String?,
        @Field("device_type") device_type: String?,
        @Field("update_first_name") update_first_name: String?,
        @Field("update_email") update_email: String?

    ): Call<ProfileResponse>

    /*

    id:100
update_last_name:sheikh
//password:123456
mobile_number:9795748340
update_mobile_number:9795748340
api_key:HiveDemo2323234001XXXWJVSILPGDSHansns002342453qwe4534vadae34
device_type:1
update_first_name:kalim
update_email:

     */
}
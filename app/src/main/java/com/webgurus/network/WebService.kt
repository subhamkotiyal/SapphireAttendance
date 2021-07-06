package com.webgurus.network

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by shubham on 3/11/17.
 *  All web services are declared here
 */

interface WebService {

    @POST("/api/v1/parent_update_profile")
    @FormUrlEncoded
    fun getTextApi(
        @Field("id")  id : String,
        @Field("update_last_name")  update_last_name : String,
        @Field("mobile_number")  mobile_number : String,
        @Field("update_mobile_number")  update_mobile_number : String,
        @Field("api_key")  api_key : String,
        @Field("device_type")  device_type : String,
        @Field("update_first_name")  update_first_name : String): Call<JsonObject>


}
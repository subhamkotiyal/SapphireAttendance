package com.app.demo2

import com.google.gson.annotations.SerializedName




class ProfileResponse {

    @SerializedName("statusCode")
    var statusCode: Int? = null

    @SerializedName("server_message")
    var server_message: String? = null
}
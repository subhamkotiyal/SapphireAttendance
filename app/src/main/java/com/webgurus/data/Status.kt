package com.webgurus.data

import com.google.gson.annotations.SerializedName

data class Status(@SerializedName("status") val status: Int, @SerializedName("message") val message: String)
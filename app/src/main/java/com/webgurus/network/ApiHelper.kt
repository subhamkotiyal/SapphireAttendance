package com.webgurus.network

import com.google.gson.GsonBuilder
import com.webgurus.Constants
import com.webgurus.data.Status
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiHelper {

    private var mRetrofit: Retrofit
    // Creating Retrofit Object
    init {

        // val token = "Bearer " + Preferences.prefs?.getValue(Constants.TOKEN, "0")
        var httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                // .addHeader("Accept","application/json")
                // .addHeader("Authorization", token)
                .build()
            chain.proceed(request)
        }
        //  val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).client(httpClient.build()).build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        httpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.interceptors().add(interceptor)


        mRetrofit = Retrofit.Builder()
            .baseUrl("https://www.askysoftware.com/hive-demo/api/v1/parent_update_profile")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }




    //Creating service class for calling the web services
    fun createService(): WebService {
        return mRetrofit.create(WebService::class.java)
    }

    // Handling error messages returned by Apis
    fun handleApiError(body: ResponseBody?): String {
        var errorMsg = Constants.SOMETHING_WENT_WRONG
        try {
            val errorConverter: Converter<ResponseBody, Status> =
                mRetrofit.responseBodyConverter(Status::class.java, arrayOfNulls(0))
            val error: Status = errorConverter.convert(body)!!
            errorMsg = error.message
        } catch (e: Exception) {
        }

        return errorMsg
    }

}
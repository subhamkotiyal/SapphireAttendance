package com.webgurus.network;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
   // private static final String BASE_URL = "http://192.168.1.161/attendance/";
   private static final String BASE_URL = "http://sapphireindustries.in/attendance/v1/";
  // private static final String BASE_URL = "http://116.193.160.116:4014/attendance/";
   //private static final String BASE_URL = "http://116.193.160.116:4014/attendance/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
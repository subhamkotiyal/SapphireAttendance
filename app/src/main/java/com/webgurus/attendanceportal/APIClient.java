package com.webgurus.attendanceportal;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class APIClient {

    private static Retrofit retrofit = null;
   public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
            // .baseUrl("http://116.193.160.116:4014/attendance/")
               .baseUrl("http://sapphireindustries.in/attendance/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }

}
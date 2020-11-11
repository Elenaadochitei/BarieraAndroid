package com.example.myapplication.retrofit;

import com.example.myapplication.ServerIp;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class RetrofitApi {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ServerIp.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()))
            .build();

    private RetrofitApi(){}
    public static Retrofit getInstance(){
        return retrofit;
    }
}

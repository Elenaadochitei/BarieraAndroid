package com.example.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {

    static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ServerIp.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

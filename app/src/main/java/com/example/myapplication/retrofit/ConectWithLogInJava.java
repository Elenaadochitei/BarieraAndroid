package com.example.myapplication.retrofit;


import com.example.myapplication.LoginResponse;
import com.example.myapplication.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ConectWithLogInJava {

    String SIGNIN_API_PATH
            = "/api/auth/signin";


    @Headers({
            "Content-type: application/json"
    })

    @POST(SIGNIN_API_PATH)
    Call<LoginResponse> checkNameAndPassword(@Body LoginRequest loginRequest);
}

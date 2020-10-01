package com.example.myapplication;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ConectWithLogInJava {

    String API_ROUTEEEEE = "getConfirmationOfUser";


    @Headers({
            "Content-type: application/json"
    })

    @GET(API_ROUTEEEEE)
    Call<LoginInfo> checkNameAndPassword (@Query("username") String username, @Query("password") String password);
}

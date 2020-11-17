package com.example.myapplication;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ConectWithLogInJava {

    String GET_CONFIRMATION_OF_USER = "getConfirmationOfUser";


    @Headers({
            "Content-type: application/json"
    })

    @GET(GET_CONFIRMATION_OF_USER)
    Call<LoginInfo> checkNameAndPassword (@Query("username") String username, @Query("password") String password);
}

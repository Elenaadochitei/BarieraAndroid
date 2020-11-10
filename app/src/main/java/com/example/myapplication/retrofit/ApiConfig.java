package com.example.myapplication.retrofit;

import com.example.myapplication.NameAndPlateRegister;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiConfig {

    @Multipart
    @POST("/putTheImgInTheFile")
    Call<String> uploadFile(@Part MultipartBody.Part file, @Part("nameAndPlateRegister") NameAndPlateRegister name);
}

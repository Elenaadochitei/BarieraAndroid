package com.example.myapplication;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public interface ConectWithJava {

    String POST_NEW_USER = "postNewUser";

    String UPDATE_DATA = "updateData/{id}";

    String DELETE_PLATE_REGISTER = "deletePlateRegister/{id}";

    String GET_ID = "getID";

    String GET_NAME_AND_PLATE_OF_USER= "getNameAndPlateOfUser/{id}";

    String SHARE_PARKING = "shareParking";

    String FREE_SHARE_PARKING = "freeShareParking";

    @Headers({
            "Content-type: application/json"
    })

    @POST(POST_NEW_USER)
    Call<NameAndPlateRegister> insertNewUser(@Body NameAndPlateRegister insertNewUser);

    @PUT(UPDATE_DATA)
    Call<String> updateUser( @Path("id") String id, @Body HashMap<String, String> updateUser);

    @DELETE(DELETE_PLATE_REGISTER)
    Call<String> deleteUser(@Path("id") String id);

    @GET(GET_ID)
    Call<String> getID(@QueryMap HashMap<String, String> getIdFromMap);

    @GET(GET_NAME_AND_PLATE_OF_USER)
    Call<List<NameAndPlateRegister>> getNameAndPlateOfUser(@Path("id") String id);

    @POST(SHARE_PARKING)
    Call<SharedParkingSpace> shareParking (@Body SharedParkingSpace sharedParkingSpace);

    @GET(FREE_SHARE_PARKING)
    Call<SharedParkingSpace> FREE_SHARE_PARKING(@Path("shared_parking_space") SharedParkingSpace sharedParkingSpace);

}


package com.example.myapplication;

import java.util.HashMap;

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

    String API_ROUTE = "postNewUser";

    String API_ROUTEE = "updateData/{id}";

    String API_ROUTEEE = "deleteData/{id}";

    String API_ROUTEEEE = "getUserByNameAndPlate";

    @Headers({
            "Content-type: application/json"
    })

    @POST(API_ROUTE)
    Call<Nume_Nr_Masina> insertNewUser(@Body Nume_Nr_Masina insertNewUser);

    @PUT(API_ROUTEE)
    Call<String> updateUser( @Path("id") String id, @Body HashMap<String, String> updateUser);

    /*
        @Body -> @RequestBody
        @Path -> @PathVariable
        @Qery -> @RequestParam
        @QueryMap ->
     */

    @DELETE(API_ROUTEEE)
    Call<String> deleteUser(@Path("id") String id);

    @GET(API_ROUTEEEE)
    Call<String> getID(@QueryMap HashMap<String, String> getIdFromMap);

}

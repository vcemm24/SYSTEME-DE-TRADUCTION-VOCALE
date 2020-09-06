package com.spokeapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceInterface {

    @GET("Firstprojet/TranslatePH.php")
    //Call<List<ModelGet>> getModel();
    //@GET("Firstprojet/test.php")
    Call<ModelGet> getModel();

    @FormUrlEncoded
    @POST("Firstprojet/TranslatePH.php")
    Call<ServiceResponse> deposer(
        @Field("texte") String parole
    );
}

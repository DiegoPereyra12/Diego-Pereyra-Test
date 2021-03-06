package com.example.axosnettestjava.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetroService {

    @GET("getall")
    Call<String> getAll();

    @POST("insert")
    Call<Void> addReceipt(@Query("provider") String provider,
                          @Query("comment") String comment,
                          @Query("amount") String amount,
                          @Query("emission_date") String emissionDate,
                          @Query("currency_code") String currencyCode);

    @POST("delete")
    Call<Void> deleteReceipt(@Query("id") Integer id);

    @POST("update")
    Call<Void> updateReceipt(@Query("id") Integer id,@Query("provider") String provider,
                          @Query("comment") String comment,
                          @Query("amount") String amount,
                          @Query("emission_date") String emissionDate,
                          @Query("currency_code") String currencyCode);
}
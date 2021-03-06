package com.example.axosnettestjava.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetroInstance {

    private static Retrofit retrofit;
    private static Retrofit retrofit2;

    public static Retrofit getRetrofit(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit postRetrofit(){
        if (retrofit2 == null){
            retrofit2 = new Retrofit.Builder()
                    .baseUrl("https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }
}
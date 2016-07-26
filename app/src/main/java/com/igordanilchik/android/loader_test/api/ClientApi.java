package com.igordanilchik.android.loader_test.api;

import com.igordanilchik.android.loader_test.model.Catalogue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClientApi {
    @GET("/getyml")
    Call<Catalogue> loadCatalogue(@Query("key") String key);
}

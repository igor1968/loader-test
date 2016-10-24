package com.igordanilchik.android.loader_test.data.source.remote;

import com.igordanilchik.android.loader_test.data.Catalogue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClientApi {
    @GET("/getyml")
    Call<Catalogue> loadCatalogue(@Query("key") String key);
}

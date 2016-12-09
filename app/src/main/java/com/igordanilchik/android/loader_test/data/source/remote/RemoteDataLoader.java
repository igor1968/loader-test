package com.igordanilchik.android.loader_test.data.source.remote;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.igordanilchik.android.loader_test.data.Catalogue;
import com.igordanilchik.android.loader_test.data.Shop;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RemoteDataLoader extends AsyncTaskLoader<Shop> {

    private static final String LOG_TAG = RemoteDataLoader.class.getSimpleName();

    private static final String API_BASE_URL = "http://ufa.farfor.ru";
    private static final String API_KEY = "ukAXxeJYZN";

    @Nullable
    private Shop data = null;

    public RemoteDataLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        } else {
            forceLoad();
        }
    }

    @Override
    public Shop loadInBackground() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        ClientApi client = retrofit.create(ClientApi.class);
        Call<Catalogue> catalogue = client.loadCatalogue(API_KEY);
        Response<Catalogue> response = null;
        try {
            response = catalogue.execute();
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Network error: ", e);
        }

        Shop result = null;
        if (response != null && response.isSuccessful()) {
            result = response.body().getShop();
        }
        return result;
    }

    @Override
    public void deliverResult(Shop data) {
        this.data = data;
        super.deliverResult(data);
    }
}

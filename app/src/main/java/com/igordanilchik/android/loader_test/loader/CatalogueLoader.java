package com.igordanilchik.android.loader_test.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.igordanilchik.android.loader_test.api.ClientApi;
import com.igordanilchik.android.loader_test.model.Catalogue;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class CatalogueLoader extends AsyncTaskLoader<Catalogue> {

    private static final String LOG_TAG = CatalogueLoader.class.getSimpleName();

    private static final String API_BASE_URL = "http://ufa.farfor.ru";
    private static final String API_KEY = "ukAXxeJYZN";

    @Nullable
    private Catalogue data = null;

    public CatalogueLoader(Context context) {
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
    public Catalogue loadInBackground() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        ClientApi client = retrofit.create(ClientApi.class);
        Call<Catalogue> catalogue = client.loadCatalogue(API_KEY);
        Catalogue result = null;
        try {
            result = catalogue.execute().body();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void deliverResult(Catalogue data) {
        this.data = data;
        super.deliverResult(data);
    }
}

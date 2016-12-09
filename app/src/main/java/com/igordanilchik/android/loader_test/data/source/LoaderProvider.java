package com.igordanilchik.android.loader_test.data.source;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.igordanilchik.android.loader_test.data.source.local.ShopPersistenceContract;

public class LoaderProvider {
    private static final String LOG_TAG = LoaderProvider.class.getSimpleName();

    @NonNull
    private final Context context;

    public LoaderProvider(@NonNull Context context) {
        this.context = context;
    }

    public Loader<Cursor> createCategoriesLoader() {
        return new CursorLoader(context, ShopPersistenceContract.CategoryEntry.buildUri(),
                ShopPersistenceContract.CategoryEntry.CATEGORY_COLUMNS,
                null,
                null,
                ShopPersistenceContract.CategoryEntry.COLUMN_NAME_TITLE + " ASC"
        );
    }

    public Loader<Cursor> createOffersLoader(int categoryId) {
        return new CursorLoader(context, ShopPersistenceContract.CategoryEntry.buildOffersUri(categoryId),
                ShopPersistenceContract.OfferEntry.OFFER_COLUMNS,
                null,
                null,
                ShopPersistenceContract.OfferEntry.COLUMN_NAME_TITLE + " ASC"
        );
    }

    public Loader<Cursor> createOfferLoader(int offerId) {
        return new CursorLoader(context, ShopPersistenceContract.OfferEntry.buildUri(offerId),
                ShopPersistenceContract.OfferEntry.OFFER_COLUMNS,
                null,
                null,
                ShopPersistenceContract.OfferEntry.COLUMN_NAME_TITLE + " ASC"
        );
    }
}

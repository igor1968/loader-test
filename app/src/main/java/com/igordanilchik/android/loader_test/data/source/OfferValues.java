package com.igordanilchik.android.loader_test.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.data.Offer;
import com.igordanilchik.android.loader_test.data.source.local.ShopPersistenceContract;


public class OfferValues {
    public static ContentValues from(@NonNull Context ctx, @NonNull Offer offer) {
        ContentValues values = new ContentValues();
        values.put(ShopPersistenceContract.OfferEntry.COLUMN_NAME_OFFER_ID, offer.getId());
        values.put(ShopPersistenceContract.OfferEntry.COLUMN_NAME_CATEGORY_ID, offer.getCategoryId());
        values.put(ShopPersistenceContract.OfferEntry.COLUMN_NAME_TITLE, offer.getName());
        values.put(ShopPersistenceContract.OfferEntry.COLUMN_NAME_PICTURE_URL, offer.getPictureUrl());
        values.put(ShopPersistenceContract.OfferEntry.COLUMN_NAME_PRICE, offer.getPrice());
        values.put(ShopPersistenceContract.OfferEntry.COLUMN_NAME_DESCRIPTION, offer.getDescription());
        if (offer.getParam() != null) {
            String weight = offer.getParam().get(ctx.getString(R.string.param_name_weight));
            if (weight != null) {
                values.put(ShopPersistenceContract.OfferEntry.COLUMN_NAME_WEIGHT, weight);
            }
        }
        return values;
    }
}

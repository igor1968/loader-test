package com.igordanilchik.android.loader_test.data.source;

import android.content.ContentValues;

import com.igordanilchik.android.loader_test.data.Category;
import com.igordanilchik.android.loader_test.data.source.local.ShopPersistenceContract;

public class CategoryValues {
    public static ContentValues from(Category category) {
        ContentValues values = new ContentValues();
        values.put(ShopPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID, category.getId());
        values.put(ShopPersistenceContract.CategoryEntry.COLUMN_NAME_TITLE, category.getTitle());
        values.put(ShopPersistenceContract.CategoryEntry.COLUMN_NAME_PICTURE_URL, category.getPictureUrl());
        return values;
    }
}

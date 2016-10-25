package com.igordanilchik.android.loader_test.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShopDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "shopList.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + ShopPersistenceContract.CategoryEntry.TABLE_NAME + " (" +
                    ShopPersistenceContract.CategoryEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    ShopPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + " UNIQUE NOT NULL" + COMMA_SEP +
                    ShopPersistenceContract.CategoryEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    ShopPersistenceContract.CategoryEntry.COLUMN_NAME_PICTURE_URL + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_OFFERS =
            "CREATE TABLE " + ShopPersistenceContract.OfferEntry.TABLE_NAME + " (" +
                    ShopPersistenceContract.OfferEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    ShopPersistenceContract.OfferEntry.COLUMN_NAME_OFFER_ID + INTEGER_TYPE + " UNIQUE NOT NULL" + COMMA_SEP +
                    ShopPersistenceContract.OfferEntry.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + COMMA_SEP +
                    ShopPersistenceContract.OfferEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    ShopPersistenceContract.OfferEntry.COLUMN_NAME_PICTURE_URL + TEXT_TYPE + COMMA_SEP +
                    ShopPersistenceContract.OfferEntry.COLUMN_NAME_WEIGHT + TEXT_TYPE + COMMA_SEP +
                    ShopPersistenceContract.OfferEntry.COLUMN_NAME_PRICE + TEXT_TYPE + COMMA_SEP +
                    ShopPersistenceContract.OfferEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY (" + ShopPersistenceContract.OfferEntry.COLUMN_NAME_CATEGORY_ID + ") " +
                    "REFERENCES " + ShopPersistenceContract.CategoryEntry.TABLE_NAME + " (" +
                    ShopPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + "))";


    public ShopDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        addCategoriesTable(db);
        addOffersTable(db);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    private void addCategoriesTable(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_CATEGORIES);
    }

    private void addOffersTable(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_OFFERS);
    }
}

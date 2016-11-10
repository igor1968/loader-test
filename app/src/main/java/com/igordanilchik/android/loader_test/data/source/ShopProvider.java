package com.igordanilchik.android.loader_test.data.source;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igordanilchik.android.loader_test.data.source.local.ShopDbHelper;
import com.igordanilchik.android.loader_test.data.source.local.ShopPersistenceContract;

import java.util.List;

public class ShopProvider extends ContentProvider {
    private static final int CATEGORY = 100;
    private static final int CATEGORY_ID = 101;
    private static final int OFFER = 200;
    private static final int OFFER_ID = 201;
    private static final int OFFER_BY_CATEGORY_ID = 202;

    private static final UriMatcher uriMatcher = buildUriMatcher();


    private ShopDbHelper dbHelper;


    public static UriMatcher buildUriMatcher() {
        String content = ShopPersistenceContract.CONTENT_AUTHORITY;
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, ShopPersistenceContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(content, ShopPersistenceContract.PATH_CATEGORY + "/#", CATEGORY_ID);
        matcher.addURI(content, ShopPersistenceContract.PATH_OFFER, OFFER);
        matcher.addURI(content, ShopPersistenceContract.PATH_OFFER + "/#", OFFER_ID);
        matcher.addURI(content, ShopPersistenceContract.PATH_CATEGORY + "/" + ShopPersistenceContract.PATH_OFFER + "/#", OFFER_BY_CATEGORY_ID);
        return matcher;

    }

    @Override
    public boolean onCreate() {
        dbHelper = new ShopDbHelper(getContext());
        return true;
    }

    @Override

    public String getType(@NonNull Uri uri) {
        switch(uriMatcher.match(uri)){
            case CATEGORY:
                return ShopPersistenceContract.CategoryEntry.CONTENT_TYPE;
            case CATEGORY_ID:
                return ShopPersistenceContract.CategoryEntry.CONTENT_ITEM_TYPE;
            case OFFER:
                return ShopPersistenceContract.OfferEntry.CONTENT_TYPE;
            case OFFER_ID:
                return ShopPersistenceContract.OfferEntry.CONTENT_ITEM_TYPE;
            case OFFER_BY_CATEGORY_ID:
                return ShopPersistenceContract.OfferEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case CATEGORY:
                retCursor = dbHelper.getReadableDatabase().query(
                        ShopPersistenceContract.CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY_ID: {
                String[] where = {uri.getLastPathSegment()};
                retCursor = dbHelper.getReadableDatabase().query(
                        ShopPersistenceContract.CategoryEntry.TABLE_NAME,
                        projection,
                        ShopPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + " = ?",
                        where,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case OFFER:
                retCursor = dbHelper.getReadableDatabase().query(
                        ShopPersistenceContract.OfferEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case OFFER_ID: {
                String[] where = {uri.getLastPathSegment()};
                retCursor = dbHelper.getReadableDatabase().query(
                        ShopPersistenceContract.OfferEntry.TABLE_NAME,
                        projection,
                        ShopPersistenceContract.OfferEntry.COLUMN_NAME_OFFER_ID + " = ?",
                        where,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case OFFER_BY_CATEGORY_ID: {
                String[] where = {uri.getLastPathSegment()};
                retCursor = dbHelper.getReadableDatabase().query(
                        ShopPersistenceContract.OfferEntry.TABLE_NAME,
                        projection,
                        ShopPersistenceContract.OfferEntry.COLUMN_NAME_CATEGORY_ID + " = ?",
                        where,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CATEGORY: {
                long id = db.insert(ShopPersistenceContract.CategoryEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    id = values.getAsInteger(ShopPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID);
                    returnUri = ShopPersistenceContract.CategoryEntry.buildUri(id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case OFFER: {
                long id = db.insert(ShopPersistenceContract.OfferEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    id = values.getAsInteger(ShopPersistenceContract.OfferEntry.COLUMN_NAME_OFFER_ID);
                    returnUri = ShopPersistenceContract.OfferEntry.buildUri(id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case CATEGORY:
                rowsDeleted = db.delete(ShopPersistenceContract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case OFFER:
                rowsDeleted = db.delete(ShopPersistenceContract.OfferEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case CATEGORY_ID: {
                List<String> segments = uri.getPathSegments();
                selection = addSelection(selection,
                        ShopPersistenceContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + " = " + segments.get(segments.size() - 1));
                rowsUpdated = db.update(ShopPersistenceContract.CategoryEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            }
            case OFFER_ID: {
                List<String> segments = uri.getPathSegments();
                selection = addSelection(selection,
                        ShopPersistenceContract.OfferEntry.COLUMN_NAME_OFFER_ID + " = " + segments.get(segments.size() - 1));
                rowsUpdated = db.update(ShopPersistenceContract.OfferEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private String addSelection(String currentSelection, final String toAdd)
    {
        if (currentSelection == null)
        {
            currentSelection = "";
        }

        if (!currentSelection.isEmpty())
        {
            currentSelection += " AND ";
        }

        return currentSelection + toAdd;
    }
}

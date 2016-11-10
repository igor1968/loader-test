package com.igordanilchik.android.loader_test.data.source.local;


import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ShopPersistenceContract {

    public static final String CONTENT_AUTHORITY = "com.igordanilchik.android.loader_test";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);

    public static final String PATH_CATEGORY = "category";
    public static final String PATH_OFFER = "offer";

    private ShopPersistenceContract() {
    }

    public static final class CategoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CATEGORY;

        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PICTURE_URL = "pictureUrl";

        public static String[] CATEGORY_COLUMNS = new String[]{
                _ID,
                COLUMN_NAME_CATEGORY_ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_PICTURE_URL};

        public static Uri buildUri(long categoryId) {
            return ContentUris.withAppendedId(CONTENT_URI, categoryId);
        }

        public static Uri buildOffersUri(long categoryId) {
            return ContentUris.withAppendedId(CONTENT_URI.buildUpon().appendPath(PATH_OFFER).build(), categoryId);
        }

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public static final int COL_ID = 0;
        public static final int COL_CATEGORY_ID = 1;
        public static final int COL_TITLE = 2;
        public static final int COL_PICTURE_URL = 3;
    }

    public static final class OfferEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_OFFER).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_OFFER;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_OFFER;

        public static final String TABLE_NAME = "offer";
        public static final String COLUMN_NAME_OFFER_ID = "offerId";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PICTURE_URL = "pictureUrl";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static String[] OFFER_COLUMNS = new String[]{
                _ID,
                COLUMN_NAME_OFFER_ID,
                COLUMN_NAME_CATEGORY_ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_PICTURE_URL,
                COLUMN_NAME_PRICE,
                COLUMN_NAME_WEIGHT,
                COLUMN_NAME_DESCRIPTION
        };

        public static Uri buildUri(long offerId) {
            return ContentUris.withAppendedId(CONTENT_URI, offerId);
        }

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public static final int COL_ID = 0;
        public static final int COL_OFFER_ID = 1;
        public static final int COL_CATEGORY_ID = 2;
        public static final int COL_TITLE = 3;
        public static final int COL_PICTURE_URL = 4;
        public static final int COL_PRICE = 5;
        public static final int COL_WEIGHT = 6;
        public static final int COL_DESCRIPTION = 7;

    }
}
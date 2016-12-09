package com.igordanilchik.android.loader_test.data.source.local;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.igordanilchik.android.loader_test.data.Category;
import com.igordanilchik.android.loader_test.data.Offer;
import com.igordanilchik.android.loader_test.data.Shop;
import com.igordanilchik.android.loader_test.data.source.CategoryValues;
import com.igordanilchik.android.loader_test.data.source.DataSource;
import com.igordanilchik.android.loader_test.data.source.OfferValues;

import java.util.ArrayList;
import java.util.List;

public class LocalDataSource implements DataSource {
    private static final String LOG_TAG = LocalDataSource.class.getSimpleName();
    private ContentResolver contentResolver;

    private static LocalDataSource INSTANCE;

    private LocalDataSource(@NonNull ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public static LocalDataSource getInstance(@NonNull ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(contentResolver);
        }
        return INSTANCE;
    }

    @Override
    @WorkerThread
    public void saveCategory(@NonNull Category category) {
        ContentValues values = CategoryValues.from(category);

        final Cursor cursor = contentResolver.query(ShopPersistenceContract.CategoryEntry.buildUri(category.getId()),
                null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                contentResolver.update(ShopPersistenceContract.CategoryEntry.buildUri(category.getId()),
                        values, null, null);
            } else {
                contentResolver.insert(ShopPersistenceContract.CategoryEntry.buildUri(category.getId()),
                        values);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    @WorkerThread
    public void saveCategories(@NonNull final List<Category> categories) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        for (Category category : categories) {
            ContentValues values = CategoryValues.from(category);
            Cursor cursor = contentResolver.query(ShopPersistenceContract.CategoryEntry.buildUri(category.getId()),
                    null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    ops.add(ContentProviderOperation.newUpdate(ShopPersistenceContract.CategoryEntry.buildUri(category.getId()))
                            .withValues(values)
                            .build());
                } else {
                    ops.add(ContentProviderOperation.newInsert(ShopPersistenceContract.CategoryEntry.buildUri())
                            .withValues(values)
                            .build());
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (ops.size() > 0) {
            try {
                contentResolver.applyBatch(ShopPersistenceContract.CONTENT_AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                Log.w(LOG_TAG, "Error during batch apply: ", e);
            }
        }
    }

    @Override
    @UiThread
    public void saveDataset(@NonNull Shop dataset) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                ArrayList<ContentProviderOperation> ops = new ArrayList<>();
                List<Category> categories = getCategories(dataset);
                if (categories != null) {
                    for (Category category : categories) {
                        ContentValues values = CategoryValues.from(category);
                        Cursor cursor = contentResolver.query(ShopPersistenceContract.CategoryEntry.buildUri(category.getId()),
                                null, null, null, null);
                        try {
                            if (cursor != null && cursor.moveToFirst()) {
                                ops.add(ContentProviderOperation.newUpdate(ShopPersistenceContract.CategoryEntry.buildUri(category.getId()))
                                        .withValues(values)
                                        .build());
                            } else {
                                ops.add(ContentProviderOperation.newInsert(ShopPersistenceContract.CategoryEntry.buildUri())
                                        .withValues(values)
                                        .build());
                            }
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }
                    }

                    List<Offer> offers = dataset.getOffers();
                    if (offers != null) {
                        for (Offer offer : offers) {
                            ContentValues values = OfferValues.from(offer);
                            Cursor cursor = contentResolver.query(ShopPersistenceContract.OfferEntry.buildUri(offer.getId()),
                                    null, null, null, null);
                            try {
                                if (cursor != null && cursor.moveToFirst()) {
                                    ops.add(ContentProviderOperation.newUpdate(ShopPersistenceContract.OfferEntry.buildUri(offer.getId()))
                                            .withValues(values)
                                            .build());
                                } else {
                                    ops.add(ContentProviderOperation.newInsert(ShopPersistenceContract.OfferEntry.buildUri())
                                            .withValues(values)
                                            .build());
                                }
                            } finally {
                                if (cursor != null) {
                                    cursor.close();
                                }
                            }
                        }

                        if (ops.size() > 0) {
                            try {
                                contentResolver.applyBatch(ShopPersistenceContract.CONTENT_AUTHORITY, ops);
                            } catch (RemoteException | OperationApplicationException e) {
                                Log.w(LOG_TAG, "Error during batch apply: ", e);
                            }
                        }
                    }
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void getDataset(@NonNull GetDatasetCallback callback) {
        // no-op since the data is loader via Cursor Loader
    }

    @Nullable
    private List<Category> getCategories(@NonNull Shop dataset) {
        List<Category> categories = dataset.getCategories();
        if (dataset.getOffers() != null) {
            List<Offer> offers = dataset.getOffers();
            for (Category category : categories) {
                for (Offer offer : offers) {
                    if (offer.getCategoryId() == category.getId() && offer.getPictureUrl() != null) {
                        category.setPictureUrl(offer.getPictureUrl());
                        break;
                    }
                }
            }
        }
        return categories;
    }
}

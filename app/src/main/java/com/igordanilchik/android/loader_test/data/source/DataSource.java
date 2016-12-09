package com.igordanilchik.android.loader_test.data.source;

import android.support.annotation.NonNull;

import com.igordanilchik.android.loader_test.data.Category;
import com.igordanilchik.android.loader_test.data.Shop;

import java.util.List;

public interface DataSource {

    interface GetDatasetCallback {

        void onDataLoaded(@NonNull Shop dataset);

        void onDataNotAvailable();
    }

    void saveCategory(@NonNull Category category);

    void saveCategories(@NonNull List<Category> categories);

    void saveDataset(@NonNull Shop dataset);

    void getDataset(@NonNull GetDatasetCallback callback);
}

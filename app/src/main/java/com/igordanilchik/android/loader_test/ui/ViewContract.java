package com.igordanilchik.android.loader_test.ui;


import com.igordanilchik.android.loader_test.data.source.LoaderProvider;

public interface ViewContract {
    void refreshData();

    void showCategory(int categoryId);

    void showOffer(int offerId);

    void showEmptyState();

    void hideEmptyState();

    LoaderProvider getLoaderProvider();
}

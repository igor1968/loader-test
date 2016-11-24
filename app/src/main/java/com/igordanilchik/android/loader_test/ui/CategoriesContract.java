package com.igordanilchik.android.loader_test.ui;


public interface CategoriesContract {
    void refreshData();
    void showCategory(int categoryId);
    void showOffer(int offerId);
    void showEmptyState();
    void hideEmptyState();
}

package com.igordanilchik.android.loader_test.data.source;

import android.support.annotation.NonNull;

import com.igordanilchik.android.loader_test.data.Category;
import com.igordanilchik.android.loader_test.data.Shop;

import java.util.List;

public interface DataSource {

    void add(@NonNull Category category);

    void add(@NonNull List<Category> categories);

    void add(@NonNull Shop dataset);
}

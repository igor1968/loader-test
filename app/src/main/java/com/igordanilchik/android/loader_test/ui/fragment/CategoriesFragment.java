package com.igordanilchik.android.loader_test.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.data.source.LoaderProvider;
import com.igordanilchik.android.loader_test.data.source.local.ShopPersistenceContract;
import com.igordanilchik.android.loader_test.ui.activity.MainActivity;
import com.igordanilchik.android.loader_test.ui.adapter.CategoriesAdapter;
import com.igordanilchik.android.loader_test.utils.DividerItemDecoration;
import com.igordanilchik.android.loader_test.utils.FragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoriesFragment extends Fragment implements CategoriesAdapter.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = CategoriesFragment.class.getSimpleName();
    private static final int CATEGORIES_LOADER = 1;

    @BindView(R.id.catalogue_recycler_view)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Unbinder unbinder;

    CategoriesAdapter cursorAdapter;
    @Nullable
    Cursor cursor;

    @NonNull
    public static CategoriesFragment newInstance() {
        CategoriesFragment f = new CategoriesFragment();
        return f;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_catalogue, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getActivity().getSupportLoaderManager().initLoader(CATEGORIES_LOADER, null, this);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        categoryClicked(position);
    }

    private void categoryClicked(int position) {
        if (cursor != null) {
            cursor.moveToPosition(position);

            int categoryId = cursor.getInt(ShopPersistenceContract.CategoryEntry.COL_CATEGORY_ID);

            Bundle args = new Bundle();
            args.putInt(MainActivity.ARG_DATA, categoryId);

            OffersFragment fragment = OffersFragment.newInstance();
            fragment.setArguments(args);
            FragmentUtils.replaceFragment(getActivity(), R.id.frame_content, fragment, true);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LoaderProvider(getActivity()).createCategoriesLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.moveToLast()) {
                cursor = data;
                //TODO: optimize
                cursorAdapter = new CategoriesAdapter(getContext(), cursor, this);
                recyclerView.setAdapter(cursorAdapter);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursor = null;
    }
}

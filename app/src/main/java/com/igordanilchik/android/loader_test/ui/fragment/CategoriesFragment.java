package com.igordanilchik.android.loader_test.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.igordanilchik.android.loader_test.LoaderTestApplication;
import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.data.source.LoaderProvider;
import com.igordanilchik.android.loader_test.ui.ViewContract;
import com.igordanilchik.android.loader_test.ui.adapter.CategoriesAdapter;
import com.igordanilchik.android.loader_test.utils.DividerItemDecoration;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoriesFragment extends Fragment implements CategoriesAdapter.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = CategoriesFragment.class.getSimpleName();
    private static final int CATEGORIES_LOADER = 1;

    @BindView(R.id.catalogue_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.empty_state_container)
    LinearLayout emptyStateContainer;

    RecyclerView.LayoutManager layoutManager;
    private Unbinder unbinder;

    CategoriesAdapter adapter;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_categories, container, false);
        this.unbinder = ButterKnife.bind(this, view);

        swipeContainer.setOnRefreshListener(this::refresh);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setRefreshing(true);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new CategoriesAdapter(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            getActivity().getSupportLoaderManager().initLoader(CATEGORIES_LOADER, null, this);
        } else {
            getActivity().getSupportLoaderManager().restartLoader(CATEGORIES_LOADER, savedInstanceState, this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.clearListener();
        recyclerView.setAdapter(null);
        //adapter = null;
        unbinder.unbind();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = LoaderTestApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onItemClick(int categoryId) {
        if (getActivity() instanceof ViewContract) {
            ((ViewContract)getActivity()).showCategory(categoryId);
        }
    }

    private void refresh() {
        if (getActivity() instanceof ViewContract) {
            ((ViewContract) getActivity()).refreshData();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        LoaderProvider provider;
        if (getActivity() instanceof ViewContract) {
            provider = ((ViewContract) getActivity()).getLoaderProvider();
        } else {
            provider = new LoaderProvider(getActivity());
        }
        return provider.createCategoriesLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (swipeContainer != null) {
            swipeContainer.setRefreshing(false);
        }
        if (data != null) {
            if (data.moveToLast()) {
                adapter.changeCursor(data);
                emptyState(false);
            } else {
                emptyState(true);
            }
        } else {
            emptyState(true);
        }
    }

    private void emptyState(boolean show){
        if (getActivity() != null && isAdded()) {
            emptyStateContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}

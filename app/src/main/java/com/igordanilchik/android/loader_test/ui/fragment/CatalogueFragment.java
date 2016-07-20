package com.igordanilchik.android.loader_test.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.model.Category;
import com.igordanilchik.android.loader_test.ui.adapter.CategoriesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CatalogueFragment extends Fragment {

    private static final String LOG_TAG = CatalogueFragment.class.getSimpleName();

    @BindView(R.id.catalogue_recycler_view)
    RecyclerView mRecyclerView;
    CategoriesAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    private Unbinder unbinder;
    private Category[] mDataset = new Category[5];

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_catalogue, container, false);
        this.unbinder = ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        for (int i = 0; i < mDataset.length; ++i)
        {
            mDataset[i] = new Category(i+1, "Title " + (i+1));
        }

        mAdapter = new CategoriesAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDataset[position].getTitle();
                Log.d(LOG_TAG, mDataset[position].getTitle());
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

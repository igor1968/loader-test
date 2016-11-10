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
import com.igordanilchik.android.loader_test.ui.CategoriesContract;
import com.igordanilchik.android.loader_test.ui.activity.MainActivity;
import com.igordanilchik.android.loader_test.ui.adapter.OffersAdapter;
import com.igordanilchik.android.loader_test.utils.DividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OffersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OffersAdapter.OnItemClickListener {

    private static final String LOG_TAG = OffersFragment.class.getSimpleName();
    private static final int OFFERS_LOADER = 2;

    @BindView(R.id.offers_recycler_view)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Unbinder unbinder;

    private int categoryId;
    @Nullable
    Cursor cursor;
    OffersAdapter cursorAdapter;

    @NonNull
    public static OffersFragment newInstance(int categoryId) {
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_CATEGORY_ID, categoryId);

        OffersFragment f = new OffersFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_offers, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MainActivity.ARG_CATEGORY_ID)) {
            categoryId = bundle.getInt(MainActivity.ARG_CATEGORY_ID);
        }

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState == null) {
            getActivity().getSupportLoaderManager().initLoader(OFFERS_LOADER, null, this);
        } else {
            getActivity().getSupportLoaderManager().restartLoader(OFFERS_LOADER, savedInstanceState, this);
        }

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        offerClicked(position);
    }

    private void offerClicked(int position) {
        if (cursor != null) {
            cursor.moveToPosition(position);

            int offerId = cursor.getInt(ShopPersistenceContract.OfferEntry.COL_OFFER_ID);
            if (getActivity() instanceof CategoriesContract) {
                ((CategoriesContract)getActivity()).showOffer(offerId);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LoaderProvider(getActivity()).createOffersLoader(categoryId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.moveToLast()) {
                cursor = data;
                if (cursorAdapter == null) {
                    cursorAdapter = new OffersAdapter(getContext(), cursor, this);
                    recyclerView.setAdapter(cursorAdapter);
                } else {
                    cursorAdapter.swapCursor(cursor);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursor = null;
    }
}

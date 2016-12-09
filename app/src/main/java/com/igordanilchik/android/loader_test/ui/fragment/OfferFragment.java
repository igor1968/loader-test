package com.igordanilchik.android.loader_test.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.data.source.LoaderProvider;
import com.igordanilchik.android.loader_test.data.source.local.ShopPersistenceContract;
import com.igordanilchik.android.loader_test.ui.ViewContract;
import com.igordanilchik.android.loader_test.ui.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OfferFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = OfferFragment.class.getSimpleName();
    private static final int OFFER_LOADER = 3;

    @BindView(R.id.card_image)
    ImageView image;
    @BindView(R.id.card_title)
    TextView title;
    @BindView(R.id.card_price)
    TextView price;
    @BindView(R.id.card_weight)
    TextView weight;
    @BindView(R.id.card_description)
    TextView description;

    private Unbinder unbinder;

    @Nullable
    Cursor cursor;
    private int offerId;

    @NonNull
    public static OfferFragment newInstance(int offerId) {
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_OFFER_ID, offerId);

        OfferFragment fragment = new OfferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MainActivity.ARG_OFFER_ID)) {
            offerId = bundle.getInt(MainActivity.ARG_OFFER_ID);
        }

        if (savedInstanceState == null) {
            getLoaderManager().initLoader(OFFER_LOADER, null, this);
        } else {
            getLoaderManager().restartLoader(OFFER_LOADER, savedInstanceState, this);
        }
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_offer, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        LoaderProvider provider;
        if (getActivity() instanceof ViewContract) {
            provider = ((ViewContract) getActivity()).getLoaderProvider();
        } else {
            provider = new LoaderProvider(getActivity());
        }
        return provider.createOfferLoader(offerId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.moveToLast()) {
                cursor = data;
                showOffer();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursor = null;
    }

    private void showOffer() {
        if (cursor != null) {
            title.setText(cursor.getString(ShopPersistenceContract.OfferEntry.COL_TITLE));
            price.setText(getString(R.string.offer_price, cursor.getString(ShopPersistenceContract.OfferEntry.COL_PRICE)));

            String weightStr = cursor.getString(ShopPersistenceContract.OfferEntry.COL_WEIGHT);
            if (weightStr != null) {
                weight.setText(getString(R.string.offer_weight, weightStr));
            }
            String url = cursor.getString(ShopPersistenceContract.OfferEntry.COL_PICTURE_URL);
            if (url != null && !url.isEmpty()) {
                Glide.with(this)
                        .load(url)
                        .fitCenter()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.ic_image_black_24dp))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);
            } else {
                image.setVisibility(View.GONE);
            }

            String descriptionText = cursor.getString(ShopPersistenceContract.OfferEntry.COL_DESCRIPTION);
            if (descriptionText != null) {
                description.setText(descriptionText);
            }
        }
    }
}

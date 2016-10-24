package com.igordanilchik.android.loader_test.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.data.Offer;
import com.igordanilchik.android.loader_test.ui.activity.MainActivity;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OfferFragment extends Fragment {
    private static final String LOG_TAG = OfferFragment.class.getSimpleName();

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
    private Offer offer;

    @NonNull
    public static OfferFragment newInstance() {
        OfferFragment f = new OfferFragment();
        return f;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MainActivity.ARG_DATA)) {
            offer = Parcels.unwrap(bundle.getParcelable(MainActivity.ARG_DATA));
        }

        if (offer != null) {
            title.setText(offer.getName());
            price.setText(getString(R.string.offer_price, offer.getPrice()));

            if (offer.getParam() != null) {
                String weightStr = offer.getParam().get(getString(R.string.param_name_weight));
                if (weightStr != null) {
                    weight.setText(getString(R.string.offer_weight, weightStr));
                }
            }

            String url = offer.getPictureUrl();
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

            String descriptionText = offer.getDescription();
            description.setText(descriptionText);
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
}

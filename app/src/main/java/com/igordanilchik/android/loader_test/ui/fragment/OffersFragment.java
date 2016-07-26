package com.igordanilchik.android.loader_test.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.model.Offer;
import com.igordanilchik.android.loader_test.ui.activity.MainActivity;
import com.igordanilchik.android.loader_test.ui.adapter.OffersAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OffersFragment extends Fragment {

    private static final String LOG_TAG = OffersFragment.class.getSimpleName();

    @BindView(R.id.offers_recycler_view)
    RecyclerView recyclerView;
    OffersAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private Unbinder unbinder;
    private int categoryId;
    @NonNull
    private List<Offer> offers = new ArrayList<>();

    @NonNull
    public static OffersFragment newInstance() {
        OffersFragment f = new OffersFragment();
        return f;
    }

    @Nullable
    private OnContentUpdate listener;

    public interface OnContentUpdate {
        @Nullable
        public List<Offer> getContent(int categoryId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContentUpdate) {
            listener = (OnContentUpdate) context;
        }
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
        categoryId = bundle.getInt(MainActivity.ARG_DATA);

        if (listener != null) {
            offers = listener.getContent(categoryId);
        }

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OffersAdapter(offers);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //categoryClicked(position);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

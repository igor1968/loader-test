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
import com.igordanilchik.android.loader_test.model.Category;
import com.igordanilchik.android.loader_test.ui.activity.MainActivity;
import com.igordanilchik.android.loader_test.ui.adapter.CategoriesAdapter;
import com.igordanilchik.android.loader_test.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoriesFragment extends Fragment {

    private static final String LOG_TAG = CategoriesFragment.class.getSimpleName();

    @BindView(R.id.catalogue_recycler_view)
    RecyclerView recyclerView;
    CategoriesAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private Unbinder unbinder;
    @NonNull
    private List<Category> categories = new ArrayList<>();

    @NonNull
    public static CategoriesFragment newInstance() {
        CategoriesFragment f = new CategoriesFragment();
        return f;
    }

    @Nullable
    private OnContentUpdate listener;

    public interface OnContentUpdate {
        @Nullable
        List<Category> getContent();
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
        final View view = inflater.inflate(R.layout.fragment_catalogue, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (listener != null && listener.getContent() != null) {
            categories = listener.getContent();
        }

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CategoriesAdapter(this.getContext(), categories);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                categoryClicked(position);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateContent(@Nullable List<Category> categories) {
        if (categories != null) {
            this.categories.clear();
            this.categories.addAll(categories);
            this.adapter.notifyDataSetChanged();
        }
    }

    private void categoryClicked(int position) {
        int categoryId = categories.get(position).getId();

        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_DATA, categoryId);

        OffersFragment fragment = OffersFragment.newInstance();
        fragment.setArguments(args);
        FragmentUtils.replaceFragment(getActivity(), R.id.frame_content, fragment, true);
    }

}

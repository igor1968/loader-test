package com.igordanilchik.android.loader_test.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.model.Category;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private static final String LOG_TAG = CategoriesAdapter.class.getSimpleName();
    private Category[] mDataset;

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener newlistener) {
        listener = newlistener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_card_view)
        CardView mCardView;
        @BindView(R.id.category_title)
        TextView mTitle;
        @BindView(R.id.category_image)
        ImageView mImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }

    public CategoriesAdapter(Category[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(mDataset[position].getTitle());
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
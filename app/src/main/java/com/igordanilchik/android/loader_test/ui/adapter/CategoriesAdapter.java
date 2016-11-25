package com.igordanilchik.android.loader_test.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.data.source.local.ShopPersistenceContract;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoriesAdapter extends CursorRecyclerAdapter<CategoriesAdapter.ViewHolder> {
    @Nullable
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int categoryId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @BindView(R.id.category_title)
        TextView title;
        @BindView(R.id.category_image)
        ImageView icon;

        public ViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public CategoriesAdapter(@Nullable OnItemClickListener listener) {
        super(null);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        // If a context is needed, it can be retrieved
        // from the ViewHolder's root view.
        Context context = holder.itemView.getContext();

        holder.title.setText(cursor.getString(ShopPersistenceContract.CategoryEntry.COL_TITLE));
        String url = cursor.getString(ShopPersistenceContract.CategoryEntry.COL_PICTURE_URL);
        Glide.with(context)
                .load(url)
                .fitCenter()
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_image_black_24dp))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.icon);

        int categoryId = cursor.getInt(ShopPersistenceContract.CategoryEntry.COL_CATEGORY_ID);

        holder.view.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(categoryId);
        });
    }

    public void clearListener() {
        listener = null;
    }
}

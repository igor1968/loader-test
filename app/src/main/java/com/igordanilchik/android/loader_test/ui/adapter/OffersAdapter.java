package com.igordanilchik.android.loader_test.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
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

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private static final String LOG_TAG = OffersAdapter.class.getSimpleName();
    @NonNull
    private Cursor cursor;
    @NonNull
    private Context context;
    @Nullable
    private final OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        @BindView(R.id.offer_name)
        TextView name;
        @BindView(R.id.offer_image)
        ImageView image;
        @BindView(R.id.offer_weight)
        TextView weight;
        @BindView(R.id.offer_price)
        TextView price;

        public ViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public OffersAdapter(@NonNull Context ctx, @NonNull Cursor cursor, @Nullable OnItemClickListener listener) {
        this.cursor = cursor;
        context = ctx;
        this.listener = listener;
    }

    @Override
    public OffersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.name.setText(cursor.getString(ShopPersistenceContract.OfferEntry.COL_TITLE));
        holder.price.setText(context.getString(R.string.offer_price, cursor.getString(ShopPersistenceContract.OfferEntry.COL_PRICE)));

        String weight = cursor.getString(ShopPersistenceContract.OfferEntry.COL_WEIGHT);
        if (weight != null) {
            holder.weight.setText(context.getString(R.string.offer_weight, weight));
        }

        String url = cursor.getString(ShopPersistenceContract.OfferEntry.COL_PICTURE_URL);
        Glide.with(context)
                .load(url)
                .fitCenter()
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_image_black_24dp))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

        holder.view.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(holder.view, position);
        });
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    public void swapCursor(@NonNull Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}

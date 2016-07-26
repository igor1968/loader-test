package com.igordanilchik.android.loader_test.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.model.Offer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private static final String LOG_TAG = CategoriesAdapter.class.getSimpleName();
    @NonNull
    private List<Offer> offers;

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener newlistener) {
        listener = newlistener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.offers_card_view)
        CardView cardView;
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

    public OffersAdapter(@NonNull List<Offer> myDataset) {
        offers = myDataset;
    }

    @Override
    public OffersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(offers.get(position).getName());
        holder.price.setText(offers.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }
}

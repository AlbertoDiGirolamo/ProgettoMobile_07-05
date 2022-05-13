package com.example.progettomobile_07_05.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettomobile_07_05.R;

public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView productImageView;
    TextView productNameTextView;
    TextView productPriceTextView;
    TextView productDescriptionTextView;
    TextView productPositionTextView;

    private OnItemListener itemListener;

    public CardViewHolder(@NonNull View itemView, OnItemListener listener) {
        super(itemView);
        productImageView = itemView.findViewById(R.id.product_imageview);
        productNameTextView = itemView.findViewById(R.id.product_name_textview);
        productPriceTextView = itemView.findViewById(R.id.product_price_textview);
        productDescriptionTextView = itemView.findViewById(R.id.product_description_textview);
        productPositionTextView = itemView.findViewById(R.id.product_position_textview);

        itemListener = listener;

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemListener.onItemClick(getAdapterPosition());
    }
}

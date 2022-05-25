package com.example.progettomobile_07_05.RecyclerView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettomobile_07_05.CardItem;
import com.example.progettomobile_07_05.R;
import com.example.progettomobile_07_05.Utilities;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {
    List<CardItem> cardItemList;
    private Activity activity;
    private OnItemListener listener;

    private List<CardItem> cardItemListNotFiltered = new ArrayList<>();

    public CardAdapter(OnItemListener listener, Activity activity){
        this.listener = listener;
        this.activity = activity;

    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return  new CardViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentCardItem = cardItemList.get(position);
        String imagePath = currentCardItem.getImageResource();
        if (imagePath.contains("ic_")){
            Drawable drawable = AppCompatResources.getDrawable(activity, activity.getResources()
                    .getIdentifier(imagePath, "drawable", activity.getPackageName()));
            holder.productImageView.setImageDrawable(drawable);
        } else {
            Bitmap bitmap = Utilities.getImageBitmap(activity, Uri.parse(imagePath));
            if (bitmap != null){
                holder.productImageView.setImageBitmap(bitmap);
            }
        }

        holder.productNameTextView.setText(currentCardItem.getProductName());
        holder.productPriceTextView.setText(currentCardItem.getProductPrice());
        holder.productDescriptionTextView.setText(currentCardItem.getProductDescription());
        holder.productPositionTextView.setText(currentCardItem.getProductPosition());
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public  void setData(List<CardItem> list){
        this.cardItemList = new ArrayList<>(list);
        this.cardItemListNotFiltered = new ArrayList<>(list);

        final CardItemDiffCallBack diffCallBack = new CardItemDiffCallBack(this.cardItemList, list);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallBack);
        diffResult.dispatchUpdatesTo(this);
    }

    public CardItem getItemSelected(int position) {
        return  cardItemList.get(position);
    }
}
